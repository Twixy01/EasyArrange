package org.example.backend.DTO.User;

import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class UserUpdateRequestMapper implements BiConsumer<UserUpdateRequest, User> {

    @Override
    public void accept(UserUpdateRequest request, User user) {
        user.setName(request.name());
        user.setEmail(request.email());
        // Password will be handled separately in the service layer to ensure it's properly encoded
        user.setProfilePicture(request.profilePicture());
        user.setRole(new Role(request.role().roleId(), request.role().name()));
    }
}