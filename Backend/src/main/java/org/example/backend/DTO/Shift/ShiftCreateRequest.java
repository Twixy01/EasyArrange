package org.example.backend.DTO.Shift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ShiftCreateRequest(
        @NotNull(message = "Shift time can't be null")
        LocalTime startShift,

        @NotNull(message = "Shift time can't be null")
        LocalTime endShift
) {
}
