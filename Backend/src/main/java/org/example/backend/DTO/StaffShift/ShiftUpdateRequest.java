package org.example.backend.DTO.StaffShift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ShiftUpdateRequest(
        @NotNull(message = "Staff ID cannot be null")
        Long staffId,
        @NotBlank(message = "Day cannot be blank")
        String day,
        @NotNull(message = "Shift time can't be null")
        LocalTime startShift,
        @NotNull(message = "Shift time can't be null")
        LocalTime endShift
) {
}
