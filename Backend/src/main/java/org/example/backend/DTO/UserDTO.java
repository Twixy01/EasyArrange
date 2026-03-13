package org.example.backend.DTO;

public record UserDTO (
        Long id,
        String name,
        String email,
        String profilePicture,
        String role
) {
}
