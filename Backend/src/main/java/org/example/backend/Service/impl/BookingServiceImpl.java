package org.example.backend.Service.impl;

import org.example.backend.Dao.BookingDao;
import org.example.backend.Service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;

    public BookingServiceImpl(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    @Override
    public List<Booking> getAllBookings() throws Exception {
        return List.of();
    }

    @Override
    public Booking getBookingById(long id) throws Exception {
        return null;
    }

    @Override
    public List<Booking> getBookingsByStaffId(long staffId) throws Exception {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByCustomerId(long customerId) throws Exception {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsBetween(LocalDateTime start, LocalDateTime end) throws Exception {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) throws Exception {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByCustomerBetween(long customerId, LocalDateTime start, LocalDateTime end) throws Exception {
        return List.of();
    }
}
