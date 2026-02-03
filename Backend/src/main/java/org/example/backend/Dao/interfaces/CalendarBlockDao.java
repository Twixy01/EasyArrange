package org.example.backend.Dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarBlockDao<E> extends Dao<E>{
    //READ
    E findCalendarBlockById(long id) throws SQLException;;
    List<E> findCalendarBlocksByStaffId(long staffId) throws SQLException;
    List<E> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) throws SQLException;
    List<E> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) throws SQLException;

}
