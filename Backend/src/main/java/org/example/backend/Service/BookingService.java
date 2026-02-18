package org.example.backend.Service;

import org.example.backend.Dao.interfaces.BookingDao;
import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Dao.jdbc.BookingDaoJdbc;
import org.example.backend.Dao.jdbc.ServiceDaoJdbc;
import org.example.backend.Dao.jdbc.UserDaoJdbc;
import org.example.backend.Entities.Booking;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final ServiceDao serviceDao;

    public BookingService(Connection connection) {
        this.bookingDao = new BookingDaoJdbc(connection);
        this.userDao = new UserDaoJdbc(connection);
        this.serviceDao = new ServiceDaoJdbc(connection);
    }

    public Booking createBooking(Booking booking) throws SQLException {
        if (booking == null) throw new IllegalArgumentException("Booking cannot be null");
        LocalDateTime start = booking.getStartDatetime();
        LocalDateTime end = booking.getEndDatetime();
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("Invalid start/end times");
        }

        if (userDao.findUserById(booking.getStaffId()) == null) {
            throw new IllegalArgumentException("Staff user not found");
        }
        if (userDao.findUserById(booking.getCustomerId()) == null) {
            throw new IllegalArgumentException("Customer user not found");
        }

        if (serviceDao.findServiceById(booking.getServiceId()) == null) {
            throw new IllegalArgumentException("Service not found");
        }

        if (bookingDao.findBookingsByStaffBetween(booking.getStaffId(), start, end).size() > 0) {
            throw new IllegalArgumentException("Staff has conflicting booking");
        }

        if (bookingDao.findBookingsByCustomerBetween(booking.getCustomerId(), start, end).size() > 0) {
            throw new IllegalArgumentException("Customer has conflicting booking");
        }
        bookingDao.create(booking);
        return booking;
    }


    public void updateBooking(Booking booking) throws SQLException {
        if (booking == null) throw new IllegalArgumentException("Booking cannot be null");
        if (bookingDao.findBookingById(booking.getId()) == null) throw new IllegalArgumentException("Booking not found");
        bookingDao.update(booking);
    }


    public void cancelBooking(long id) throws SQLException {
        Booking existing = bookingDao.findBookingById(id);
        if (existing == null) throw new IllegalArgumentException("Booking not found");
        bookingDao.remove(existing);
    }


    public Booking getBookingById(long id) throws SQLException {
        return bookingDao.findBookingById(id);
    }


    public List<Booking> listAllBookings() throws SQLException {
        return bookingDao.findAll();
    }
}

