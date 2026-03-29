package org.example.backend.DTO.Staff;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaffRequest(
        @NotNull(message = "User ID cannot be null")
        Long userId,
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,
        String bio
) {
}
