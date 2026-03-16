package org.example.backend.DTO;

import org.example.backend.Model.entity.User;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class UserUpdateRequestMapper implements Function<UserUpdateRequest, User> {
    @Override
    public User apply(UserUpdateRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setProfilePicture(request.profilePicture());
        // Role will be set in the service layer based on roleId
        return user;
    }
}
