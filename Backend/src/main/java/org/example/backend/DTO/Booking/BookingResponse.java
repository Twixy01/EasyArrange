package org.example.backend.DTO.Booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.User.UserResponse;

import java.time.LocalDateTime;

public record BookingResponse (
    Long bookingId,
    StaffResponse staff,
    UserResponse user,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime startDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime endDateTime,
    ServiceResponse service,
    String status
) {
}
