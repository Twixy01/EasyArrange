package org.example.backend.DTO.TimeSlot;

import java.time.LocalDateTime;

public record AvailableSlotResponse(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String label
) {
}
