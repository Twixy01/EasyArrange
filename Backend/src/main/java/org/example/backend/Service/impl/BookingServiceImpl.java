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
    public List<Booking> getBookingsByStaffId(int staffId) {
        return bookingDao.findBookingsByStaffId(staffId);
    }

    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) {
        return bookingDao.findBookingsByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.findBookingsBetween(startTime, endTime);
    }

    @Override
    public List<Booking> getBookingsByStaffBetween(int staffId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.findBookingsByStaffBetween(staffId, startTime, endTime);
    }

    @Override
    public List<Booking> getBookingsByCustomerBetween(int customerId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.findBookingsByCustomerBetween(customerId, startTime, endTime);
    }
}
