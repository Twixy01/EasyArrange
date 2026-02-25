package org.example.backend.Service.impl;

import org.example.backend.Model.entity.Staff;
import org.example.backend.Service.StaffService;

public class StaffServiceImpl implements StaffService {
    private final StaffService staffService;

    public StaffServiceImpl(StaffService staffService) {
        this.staffService = staffService;
    }


    @Override
    public Staff getAllUserById(long userId) {
        return null;
    }

    @Override
    public Staff getByShiftId(long shiftId) {
        return null;
    }
}
