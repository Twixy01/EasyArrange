package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.BookingDao;
import org.example.backend.Entities.Booking;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDaoJdbc extends JdbcConnection implements BookingDao<Booking> {
    public BookingDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public Booking findBookingById(long id) throws SQLException {
        PreparedStatement bookingById = connection.prepareStatement("SELECT * FROM booking WHERE id = ?");
        bookingById.setLong(1, id);
        ResultSet rs = bookingById.executeQuery();

        if (rs.next()) {
            return new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            );
        }
        return null;
    }

    @Override
    public List<Booking> findBookingsByStaffId(long staffId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement bookingsByStaffId = connection.prepareStatement("SELECT * FROM booking WHERE staff_id = ?");
        bookingsByStaffId.setLong(1, staffId);
        ResultSet rs = bookingsByStaffId.executeQuery();
        while (rs.next()) {
            bookings.add(new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            ));
        }
        return bookings;
    }

    @Override
    public List<Booking> findBookingsByCustomerId(long customerId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement bookingsByCustomerId = connection.prepareStatement("SELECT * FROM booking WHERE customer_id = ?");
        bookingsByCustomerId.setLong(1, customerId);
        ResultSet rs = bookingsByCustomerId.executeQuery();
        while (rs.next()) {
            bookings.add(new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            ));
        }
        return bookings;
    }

    @Override
    public List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM booking WHERE start_datetime >= ? AND end_datetime <= ?");
        blockByStaffId.setTimestamp(1, Timestamp.valueOf(start));
        blockByStaffId.setTimestamp(2, Timestamp.valueOf(end));
        ResultSet rs = blockByStaffId.executeQuery();

        while (rs.next()){
            bookings.add(new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            ));
        }
        return bookings;
    }

    @Override
    public List<Booking> findBookingsByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement bookingsByStaffBetween = connection.prepareStatement("SELECT * FROM booking WHERE staff_id = ? AND start_datetime >= ? AND end_datetime <= ?");
        bookingsByStaffBetween.setLong(1, staffId);
        bookingsByStaffBetween.setTimestamp(2, Timestamp.valueOf(start));
        bookingsByStaffBetween.setTimestamp(3, Timestamp.valueOf(end));
        ResultSet rs = bookingsByStaffBetween.executeQuery();

        while (rs.next()){
            bookings.add(new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            ));
        }
        return bookings;
    }

    @Override
    public List<Booking> findBookingsByCustomerBetween(long customerId, LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement bookingsByStaffBetween = connection.prepareStatement("SELECT * FROM booking WHERE customer_id = ? AND start_datetime >= ? AND end_datetime <= ?");
        bookingsByStaffBetween.setLong(1, customerId);
        bookingsByStaffBetween.setTimestamp(2, Timestamp.valueOf(start));
        bookingsByStaffBetween.setTimestamp(3, Timestamp.valueOf(end));
        ResultSet rs = bookingsByStaffBetween.executeQuery();

        while (rs.next()){
            bookings.add(new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            ));
        }
        return bookings;
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        Statement findAll = connection.createStatement();
        ResultSet rs = findAll.executeQuery("SELECT * FROM booking");
        while (rs.next()) {
            bookings.add(new Booking(
                    rs.getLong("id"),
                    rs.getLong("staff_id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("service_id")
            ));
        }
        return bookings;
    }
}
