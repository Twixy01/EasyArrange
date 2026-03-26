package org.example.backend.DTO.Shift;

import org.example.backend.Model.entity.ShiftDay;

import java.time.LocalTime;

public record ShiftResponse(
        Long shiftId,
        ShiftDay day,
        LocalTime startShift,
        LocalTime endShift
) {
}
