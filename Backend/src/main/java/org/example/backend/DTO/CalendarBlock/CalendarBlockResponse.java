package org.example.backend.DTO.CalendarBlock;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.backend.DTO.Staff.StaffResponse;

import java.time.LocalDateTime;

public record CalendarBlockResponse(
        Long calendarBlockId,
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endDateTime,
        StaffResponse staff
) {
}
