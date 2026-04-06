package org.example.backend.DTO.Shift;

import org.example.backend.Model.entity.ShiftDay;

import java.time.LocalTime;

public record ShiftResponse(
        Long shiftId,
        String day,
        LocalTime startShift,
        LocalTime endShift
) {
}
