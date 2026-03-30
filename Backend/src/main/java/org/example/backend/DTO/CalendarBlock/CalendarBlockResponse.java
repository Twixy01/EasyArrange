package org.example.backend.DTO.CalendarBlock;

import org.example.backend.DTO.Staff.StaffResponse;

import java.time.LocalDateTime;

public record CalendarBlockResponse(
        Long calendarBlockId,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        StaffResponse staff
) {
}
