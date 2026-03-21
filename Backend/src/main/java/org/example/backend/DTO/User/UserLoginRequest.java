package org.example.backend.DTO.User;

public record UserLoginRequest(
        String email,
        String password
) {
}
