package org.example.backend.DTO.StaffShift;

import org.example.backend.Model.entity.StaffShift;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffShiftResponseMapper implements Function<StaffShift, StaffShiftResponse> {
    @Override
    public StaffShiftResponse apply(StaffShift staffShift) {
        return new StaffShiftResponse(
                staffShift.getId().getStaffId(),
                staffShift.getId().getShiftId()
        );
    }
}
