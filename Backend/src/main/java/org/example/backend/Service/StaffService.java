package org.example.backend.Service;

import org.example.backend.Model.entity.Staff;

public interface StaffService {
    Staff getAllUserById(long userId);
    Staff getByShiftId(long shiftId);
}
