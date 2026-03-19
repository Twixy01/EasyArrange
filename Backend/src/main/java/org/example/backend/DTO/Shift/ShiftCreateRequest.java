package org.example.backend.DTO.Shift;

import jakarta.validation.constraints.NotNull;
import org.example.backend.Model.entity.ShiftDay;

import java.time.LocalTime;

public record ShiftCreateRequest(
        @NotNull(message = "Shift day can't be null")
        ShiftDay day,

        @NotNull(message = "Shift time can't be null")
        LocalTime startShift,

        @NotNull(message = "Shift time can't be null")
        LocalTime endShift
) {
}
