package org.example.backend.DTO.Service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ServiceRequest(
        @NotBlank(message = "Service name can't be blank")
        String name,
        @NotNull(message = "Price can't be null")
        @Positive(message = "Price must be positive")
        Integer price,
        @NotNull(message = "Duration can't be null")
        @Positive(message = "Duration must be positive")
        Integer duration,
        String description,
        String image
) {
}
