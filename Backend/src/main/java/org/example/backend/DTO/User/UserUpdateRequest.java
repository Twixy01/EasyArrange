package org.example.backend.DTO.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.DTO.Role.RoleResponse;

public record UserUpdateRequest(
        @NotBlank(message = "Name must not be blank")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Current password must not be blank")
        String currentPassword,

        @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
        String newPassword,

        String profilePicture,

        @NotNull(message = "Role must be specified")
        RoleResponse role
){
}
