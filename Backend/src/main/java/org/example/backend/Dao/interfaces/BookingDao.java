package org.example.backend.Dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDao<E> extends Dao<E>{

    //READ
    E findBookingById(long id) throws SQLException;
    List<E> findBookingsByStaffId(long staffId) throws SQLException;
    List<E> findBookingsByCustomerId(long customerId) throws SQLException;
    List<E> findBookingsBetween(LocalDateTime start, LocalDateTime end) throws SQLException;
    List<E> findBookingsByStaffBetween(long staffId,LocalDateTime start, LocalDateTime end) throws SQLException;
    List<E> findBookingsByCustomerBetween(long customerId,LocalDateTime start, LocalDateTime end) throws SQLException;

}
