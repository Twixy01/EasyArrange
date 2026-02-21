package org.example.backend.Dao;

import org.example.backend.Model.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDao extends Dao<Booking>{
    Booking findBookingById(long id);
    List<Booking> findBookingsByStaffId(long staffId);
    List<Booking> findBookingsByCustomerId(long customerId);
    List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end);
    List<Booking> findBookingsByStaffBetween(long staffId,LocalDateTime start, LocalDateTime end);
    List<Booking> findBookingsByCustomerBetween(long customerId,LocalDateTime start, LocalDateTime end);
}
