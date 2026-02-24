package org.example.backend.Dao;

import org.example.backend.Model.entity.Shift;

import java.time.LocalTime;
import java.util.List;

public interface ShiftDao extends Dao<Shift> {
    List<Shift> findAllShiftsByStaffId(long staffId);
    List<Shift> findAllShiftsByTime(LocalTime time);
    List<Shift> findShiftsBetweenTime(LocalTime startTime, LocalTime endTime);
    List<Shift> findShiftsByStaffIdBetweenDates(long staff_id, LocalTime startTime, LocalTime endTime);
}
