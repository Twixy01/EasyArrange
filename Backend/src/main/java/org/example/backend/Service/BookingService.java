package org.example.backend.Service;

import java.util.List;

public interface BookingService {

    List<Booking> getAllBookings() throws Exception;

    Booking getBookingById(long id) throws Exception;

    List<Booking> getBookingsByStaffId(long staffId) throws Exception;

    List<Booking> getBookingsByCustomerId(long customerId) throws Exception;

    List<Booking> getBookingsBetween(
            java.time.LocalDateTime start, java.time.LocalDateTime end) throws Exception;

    List<Booking> getBookingsByStaffBetween(
            long staffId, java.time.LocalDateTime start, java.time.LocalDateTime end) throws Exception;

    List<Booking> getBookingsByCustomerBetween(
            long customerId, java.time.LocalDateTime start, java.time.LocalDateTime end) throws Exception;

}
