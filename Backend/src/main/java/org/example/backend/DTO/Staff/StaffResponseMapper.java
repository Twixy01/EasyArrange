package org.example.backend.DTO.Staff;

import org.example.backend.DTO.User.UserResponseMapper;
import org.example.backend.Model.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffResponseMapper implements Function<Staff, StaffResponse> {
    private final UserResponseMapper userResponseMapper;

    public StaffResponseMapper(UserResponseMapper userResponseMapper) {
        this.userResponseMapper = userResponseMapper;
    }

    @Override
    public StaffResponse apply(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                userResponseMapper.apply(staff.getUser()),
                staff.getTitle(),
                staff.getBio()
        );
    }
}
