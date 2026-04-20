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

    @Query("FROM Booking b WHERE b.startDateTime >= :start AND b.endDateTime <= :end")
    List<Booking> findAllBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Booking b WHERE b.staff.id = :staffId AND b.startDateTime >= :start AND b.endDateTime <= :end")
    List<Booking> findAllByStaffBetween(@Param("staffId") Long staffId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Booking b WHERE b.customer.id = :customerId AND b.startDateTime >= :start AND b.endDateTime <= :end")
    List<Booking> findAllByCustomerBetween(@Param("customerId") Long customerId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Booking b WHERE b.status = :status")
    List<Booking> findAllBookingsByStatus(@Param ("status") BookingStatus status);

    @Query("FROM Booking b ORDER BY b.startDateTime ASC")
    List<Booking> findAllByOrderByStartDateTimeAsc();

    @Query("SELECT b FROM Booking b ORDER BY b.startDateTime DESC")
    List<Booking> findAllByOrderByStartDateTimeDesc();

    @Query("""
            SELECT DISTINCT b FROM Booking b
            JOIN FETCH b.staff s
            JOIN FETCH s.user
            LEFT JOIN FETCH s.services ss
            LEFT JOIN FETCH ss.service
            JOIN FETCH b.customer
            JOIN FETCH b.service
            WHERE b.staff.id = :staffId
            AND :start < b.endDateTime
            AND :end > b.startDateTime
    """)
    List<Booking> findAllOverlaps(@Param("staffId")Long staffId, @Param("start")LocalDateTime start, @Param("end")LocalDateTime end);

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Booking b WHERE b.staff.id = :staffId
                AND :start < b.endDateTime
                AND :end > b.startDateTime
    """)
    boolean existsOverlapping(@Param("staffId")Long staffId, @Param("start")LocalDateTime start, @Param("end")LocalDateTime end);

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END
        FROM Booking b WHERE b.staff.id = :staffId
                AND b.startDateTime = :start
                AND b.endDateTime = :end
    """)
    boolean existsByStaffIdAndStartDateTimeAndEndDateTime(Long staffId, LocalDateTime start, LocalDateTime end);
}
