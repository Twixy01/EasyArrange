package org.example.backend.DTO.User;

import jakarta.validation.constraints.*;
import org.example.backend.DTO.Role.RoleResponse;

public record UserRegistrationRequest(
        @NotBlank(message = "Name must not be blank")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Invalid email format")
        String email,

        @Pattern(
                regexp = "^(\\+36|0036|06)(1|[2-9][0-9])\\d{7}$",
                message = "Invalid Hungarian phone number"
        )
        String phoneNumber,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
        String password,

        @NotNull(message = "Role must be specified")
        RoleResponse role
) {
}
