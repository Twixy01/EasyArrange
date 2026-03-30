package org.example.backend.DTO.StaffShift;

import org.example.backend.DTO.Shift.ShiftResponseMapper;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.Model.entity.StaffShift;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffShiftResponseMapper implements Function<StaffShift, StaffShiftResponse> {
    private final StaffResponseMapper staffResponseMapper;
    private final ShiftResponseMapper shiftResponseMapper;

    public StaffShiftResponseMapper(StaffResponseMapper staffResponseMapper, ShiftResponseMapper shiftResponseMapper) {
        this.staffResponseMapper = staffResponseMapper;
        this.shiftResponseMapper = shiftResponseMapper;
    }

    @Override
    public StaffShiftResponse apply(StaffShift staffShift) {
        return new StaffShiftResponse(
                staffResponseMapper.apply(staffShift.getStaff()),
                shiftResponseMapper.apply(staffShift.getShift())
        );
    }
}
