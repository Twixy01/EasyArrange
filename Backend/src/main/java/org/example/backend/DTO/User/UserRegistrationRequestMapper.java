package org.example.backend.DTO.User;

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
        user.setPassword(request.password());
        // Role will be set in the service layer based on roleId
        return user;
    }
}
