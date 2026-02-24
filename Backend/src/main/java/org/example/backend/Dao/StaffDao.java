package org.example.backend.Dao;

import org.example.backend.Model.entity.Staff;

public interface StaffDao extends Dao<Staff>{
    Staff findByUserId(long userId);
    Staff findByShiftId(long shiftId);
}
