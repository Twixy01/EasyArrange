package org.example.backend.DTO.Staff;

import org.example.backend.Model.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffRequestMapper implements Function<StaffRequest, Staff> {
    @Override
    public Staff apply(StaffRequest staffRequest) {
        Staff staff = new Staff();
        staff.setTitle(staffRequest.title());
        staff.setBio(staffRequest.bio());
        return staff;
    }
}
