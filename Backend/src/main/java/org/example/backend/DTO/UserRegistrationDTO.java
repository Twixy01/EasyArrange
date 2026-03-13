package org.example.backend.DTO;

public record UserRegistrationDTO(
        String name,
        String email,
        String password,
        String profilePicture,
        Long roleId
) {
}
