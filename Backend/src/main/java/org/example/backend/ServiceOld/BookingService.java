package org.example.backend.Service;

import org.example.backend.Model.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<Booking> getBookingsByStaffId(int staffId);
    List<Booking> getBookingsByCustomerId(int customerId);
    List<Booking> getBookingsBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> getBookingsByStaffBetween(int staffId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> getBookingsByCustomerBetween(int customerId, LocalDateTime startTime, LocalDateTime endTime);
}
