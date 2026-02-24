package org.example.backend.Service.impl;

import org.example.backend.Dao.BookingDao;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;

    public BookingServiceImpl(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    @Override
    public List<Booking> getBookingsByStaffId(int staff_id) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByCustomerId(int customer_id) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByStaffBetween(int staff_id, LocalDateTime startTime, LocalDateTime endTime) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByCustomerBetween(int customer_id, LocalDateTime startTime, LocalDateTime endTime) {
        return List.of();
    }
}
