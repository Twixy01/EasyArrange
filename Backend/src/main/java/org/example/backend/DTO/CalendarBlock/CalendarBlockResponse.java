package org.example.backend.DTO.CalendarBlock;

import java.time.LocalDateTime;

public record CalendarBlockResponse(
        Long calendarBlockId,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        Long staffId
) {
}
