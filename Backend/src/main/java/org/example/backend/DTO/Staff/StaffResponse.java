package org.example.backend.DTO.Staff;

import org.example.backend.DTO.User.UserResponse;

public record StaffResponse(
        Long staffId,
        UserResponse user,
        String title,
        String bio
) {
}
