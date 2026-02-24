package org.example.backend.Service;

import org.example.backend.Model.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    List<Booking> getBookingsByStaffId(int staff_id);
    List<Booking> getBookingsByCustomerId(int customer_id);
    List<Booking> getBookingsBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> getBookingsByStaffBetween(int staff_id, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> getBookingsByCustomerBetween(int customer_id, LocalDateTime startTime, LocalDateTime endTime);

}
