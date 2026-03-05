package org.example.backend.Service;

import org.example.backend.Model.entity.CalendarBlock;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarBlockService {
    List<CalendarBlock> getCalendarBlocksByStaffId(int staffId);
    List<CalendarBlock> getCalendarBlocksBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<CalendarBlock> getCalendarBlocksByStaffIdBetween(int staffId, LocalDateTime startDate, LocalDateTime endDate);
}
