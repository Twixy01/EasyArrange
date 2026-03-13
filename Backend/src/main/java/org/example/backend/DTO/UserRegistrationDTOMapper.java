package org.example.backend.DTO;

import org.example.backend.Model.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRegistrationDTOMapper implements Function<User, UserRegistrationDTO> {
    @Override
    public UserRegistrationDTO apply(User user) {
        return new UserRegistrationDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfilePicture(),
                user.getRole().getId()
        );
    }
}
