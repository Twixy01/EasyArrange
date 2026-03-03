package org.example.backend.Repository;

import org.example.backend.Model.entity.Shift;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffShift;

import java.util.List;

public interface StaffShiftDao extends Dao<StaffShift>{
    List<Shift> findAllShiftsByStaffId(long staffId);
    List<Staff> findAllStaffByShiftId(long shiftId);
}
