package org.example.backend.DTO.Booking;

import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.User.UserResponse;

import java.time.LocalDateTime;

public record BookingResponse (
    Long bookingId,
    StaffResponse staff,
    UserResponse customer,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    ServiceResponse service,
    String status
) {

}
