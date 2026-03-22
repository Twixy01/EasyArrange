package org.example.backend.DTO.Staff;

import org.example.backend.Model.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffResponseMapper implements Function<Staff, StaffResponse> {
    @Override
    public StaffResponse apply(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getUser().getId()
        );
    }
}
