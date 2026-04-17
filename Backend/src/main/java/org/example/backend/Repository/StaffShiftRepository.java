package org.example.backend.Repository;

import org.example.backend.Model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffShiftRepository extends JpaRepository<StaffShift, StaffShiftId> {
    @Query("SELECT ss.shift FROM StaffShift ss WHERE ss.id.staffId = :staffId")
    List<Shift> findAllShiftsByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT ss.staff FROM StaffShift ss WHERE ss.id.shiftId = :shiftId")
    List<Staff> findAllStaffByShiftId(@Param("shiftId") Long shiftId);

    @Query("SELECT ss.shift FROM StaffShift ss WHERE ss.staff.id = :staffId AND" +
            " ss.shift.startShift >= :startShift AND ss.shift.endShift <= :endShift")
    List<Shift> findAllShiftsByStaffIdBetweenShifts(
            @Param("staffId") Long staffId,
            @Param("startShift") LocalTime startShift,
            @Param("endShift") LocalTime endShift
    );

    @Query("SELECT ss FROM StaffShift ss WHERE ss.staff.id = :staffId AND ss.shift.day = :day")
    Optional<StaffShift> findStaffShiftByStaffAndShiftDay(@Param("staffId")Long staffId, @Param("day") ShiftDay day);

}
