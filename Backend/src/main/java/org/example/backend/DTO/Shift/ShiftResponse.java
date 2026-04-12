package org.example.backend.DTO.Shift;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record ShiftResponse(
        Long shiftId,
        String day,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startShift,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endShift
) {
}
