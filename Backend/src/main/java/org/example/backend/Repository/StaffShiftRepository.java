package org.example.backend.Repository;

import org.example.backend.Model.entity.StaffShift;
import org.example.backend.Model.entity.StaffShiftId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffShiftRepository extends JpaRepository<StaffShift, StaffShiftId> {
    @Query("SELECT ss FROM StaffShift ss WHERE ss.id.staffId = :staffId")
    List<StaffShift> findAllShiftsByStaffId(long staffId);

    @Query("SELECT ss FROM StaffShift ss WHERE ss.id.shiftId = :shiftId")
    List<StaffShift> findAllStaffByShiftId(long shiftId);
}
