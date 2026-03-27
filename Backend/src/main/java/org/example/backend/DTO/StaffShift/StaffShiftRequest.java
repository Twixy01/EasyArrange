package org.example.backend.DTO.StaffShift;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StaffShiftRequest(
        @NotNull(message = "Staff ID cannot be null")
        @Positive(message = "Staff ID must be a positive number")
        Long staffId,
        @NotNull(message = "Shift ID cannot be null")
        @Positive(message = "Shift ID must be a positive number")
        Long shiftId
) {
}
