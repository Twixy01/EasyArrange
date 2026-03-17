package org.example.backend.DTO.User;

public record UserResponse(
        Long userId,
        String name,
        String email,
        String profilePicture,
        String role
) {
}
