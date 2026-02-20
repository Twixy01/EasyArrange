package org.example.backend.Service;

import org.example.backend.Model.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    List<Booking> getAllBookings() throws Exception;

    Booking getBookingById(long id) throws Exception;

    List<Booking> getBookingsByStaffId(long staffId) throws Exception;

    List<Booking> getBookingsByCustomerId(long customerId) throws Exception;

    List<Booking> getBookingsBetween(
            LocalDateTime start, LocalDateTime end) throws Exception;

    List<Booking> getBookingsByStaffBetween(
            long staffId, LocalDateTime start, LocalDateTime end) throws Exception;

    List<Booking> getBookingsByCustomerBetween(
            long customerId, LocalDateTime start, LocalDateTime end) throws Exception;

}
