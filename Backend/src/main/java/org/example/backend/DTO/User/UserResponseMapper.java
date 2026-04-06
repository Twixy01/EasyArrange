package org.example.backend.DTO.User;

import org.example.backend.DTO.Role.RoleResponse;
import org.example.backend.Model.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResponseMapper implements Function<User, UserResponse> {
    @Override
    public UserResponse apply(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfilePicture(),
                new RoleResponse(user.getRole().getRoleId(), user.getRole().getName())
        );
    }
}
