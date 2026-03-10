package org.example.backend.Repository;

import org.example.backend.Model.entity.Booking;
import org.example.backend.Model.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByStaffId(Long staffId);

    List<Booking> findAllByCustomerId(Long customerId);

    @Query("FROM Booking b WHERE b.startDatetime >= :start AND b.endDatetime <= :end")
    List<Booking> findAllBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Booking b WHERE b.staff.id = :staffId AND b.startDatetime >= :start AND b.endDatetime <= :end")
    List<Booking> findAllByStaffBetween(@Param("staffId") Long staffId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Booking b WHERE b.customer.id = :customerId AND b.startDatetime >= :start AND b.endDatetime <= :end")
    List<Booking> findAllByCustomerBetween(@Param("customerId") Long customerId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Booking b WHERE b.status = :status")
    List<Booking> findAllBookingsByStatus(@Param ("status") BookingStatus status);

}
