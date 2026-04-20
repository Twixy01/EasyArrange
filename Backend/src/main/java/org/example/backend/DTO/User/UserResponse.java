package org.example.backend.DTO.User;

import org.example.backend.DTO.Role.RoleResponse;

public record UserResponse(
        Long userId,
        String name,
        String email,
        String phoneNumber,
        String profilePicture,
        RoleResponse role
) {
}
