package org.example.backend.Repository;

import org.example.backend.Model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByStaffId(long staffId);
    List<Booking> findBookingsByCustomerId(long customerId);
    List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end);
    List<Booking> findBookingsByStaffBetween(long staffId,LocalDateTime start, LocalDateTime end);
    List<Booking> findBookingsByCustomerBetween(long customerId,LocalDateTime start, LocalDateTime end);
    List<Booking> findAll();
}
