package org.example.backend.DTO.Booking;

import java.time.LocalDateTime;

public record BookingResponse (
    Long bookingId,
    Long staffId,
    Long customerId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    Long serviceId,
    String status
) {

}
