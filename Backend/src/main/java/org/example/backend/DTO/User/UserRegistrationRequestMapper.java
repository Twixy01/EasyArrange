package org.example.backend.DTO.User;

import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRegistrationRequestMapper implements Function<UserRegistrationRequest, User> {
    @Override
    public User apply(UserRegistrationRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        // Password will be handled separately in the service layer to ensure it's properly encoded
        user.setRole(new Role(request.role().roleId(), request.role().name()));
        return user;
    }
}
