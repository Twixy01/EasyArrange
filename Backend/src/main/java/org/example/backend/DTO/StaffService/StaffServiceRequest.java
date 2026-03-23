package org.example.backend.DTO.StaffService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StaffServiceRequest(
        @NotNull(message = "Staff ID cannot be null")
        @Positive(message = "Staff ID must be a positive number")
        Long staffId,
        @NotNull(message = "Service ID cannot be null")
        @Positive(message = "Service ID must be a positive number")
        Long serviceId
) {
}
