package org.example.backend.DTO.Booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingCreateRequest (
        @NotNull(message = "Staff ID must be specified")
        Long staffId,

        @NotNull(message = "User ID must be specified")
        Long userId,

        @NotNull(message = "Booking time can't be null")
        LocalDateTime startDateTime,

        @NotNull(message = "Booking time can't be null")
        LocalDateTime endDateTime,

        @NotNull(message = "Service ID must be specified")
        Long serviceId
){
}
