package org.example.backend.Dao;

import org.example.backend.Model.entity.CalendarBlock;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarBlockDao extends Dao<CalendarBlock>{
    CalendarBlock findCalendarBlockById(long id);
    List<CalendarBlock> findCalendarBlocksByStaffId(long staffId);
    List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end);
    List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end);

}
