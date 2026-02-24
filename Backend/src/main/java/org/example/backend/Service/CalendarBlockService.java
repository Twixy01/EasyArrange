package org.example.backend.Service;

import org.example.backend.Model.entity.CalendarBlock;

import java.util.List;

public interface CalendarBlockService {
    List<CalendarBlock> getCalendarBlocksByStaffId(int staff_id);
    List<CalendarBlock> getCalendarBlocksBetween(String date);
    List<CalendarBlock> getCalendarBlocksByStaffIdBetween(int staff_id, String startDate, String endDate);
}
