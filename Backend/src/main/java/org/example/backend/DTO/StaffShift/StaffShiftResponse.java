package org.example.backend.DTO.StaffShift;

import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.DTO.Staff.StaffResponse;

public record StaffShiftResponse(
        StaffResponse staff,
        ShiftResponse shift
) {
}
