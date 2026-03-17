package org.example.backend.DTO.Booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingUpdateRequest (
        @NotBlank(message = "Booking time must not be blank")
        LocalDateTime startDateTime,

        @NotBlank(message = "Booking time must not be blank")
        LocalDateTime endDateTime,

        @NotNull(message = "Service ID must be specified")
        Long serviceId,

        @NotBlank(message = "Status must not be blank")
        String status

){
}
