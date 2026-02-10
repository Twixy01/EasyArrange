package org.example.backend.Dao.interfaces;

import org.example.backend.Entities.Booking;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDao extends Dao<Booking>{
    Booking findBookingById(long id) throws SQLException;
    List<Booking> findBookingsByStaffId(long staffId) throws SQLException;
    List<Booking> findBookingsByCustomerId(long customerId) throws SQLException;
    List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end) throws SQLException;
    List<Booking> findBookingsByStaffBetween(long staffId,LocalDateTime start, LocalDateTime end) throws SQLException;
    List<Booking> findBookingsByCustomerBetween(long customerId,LocalDateTime start, LocalDateTime end) throws SQLException;
}
