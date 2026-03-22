package org.example.backend.DTO.Staff;

import jakarta.validation.constraints.NotNull;

public record StaffRequest(
        @NotNull(message = "User ID cannot be null")
        Long userId
) {
}
