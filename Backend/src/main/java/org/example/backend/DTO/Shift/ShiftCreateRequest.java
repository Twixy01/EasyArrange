package org.example.backend.DTO.Shift;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record ShiftCreateRequest(
        @NotBlank(message = "Shift time must not be blank")
        LocalTime startShift,

        @NotBlank(message = "Shift time must not be blank")
        LocalTime endShift
) {
}
