package org.example.backend.DTO.Staff;

import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.User.UserResponse;

import java.util.List;

public record StaffResponse(
        Long staffId,
        UserResponse user,
        String title,
        String bio,
        List<ServiceResponse> services
) {
}
