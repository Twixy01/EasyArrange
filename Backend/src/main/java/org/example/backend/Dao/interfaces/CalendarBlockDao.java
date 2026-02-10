package org.example.backend.Dao.interfaces;

import org.example.backend.Entities.CalendarBlock;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarBlockDao extends Dao<CalendarBlock>{
    CalendarBlock findCalendarBlockById(long id) throws SQLException;
    List<CalendarBlock> findCalendarBlocksByStaffId(long staffId) throws SQLException;
    List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) throws SQLException;
    List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) throws SQLException;

}
