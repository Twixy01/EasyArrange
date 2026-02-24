package org.example.backend.Dao;

import org.example.backend.Model.entity.Shift;

import java.time.LocalTime;
import java.util.List;

public interface ShiftDao extends Dao<Shift> {
    List<Shift> findAllShiftsByStaffId(int staff_id);
    List<Shift> findAllShiftsByDate(LocalTime time);
    List<Shift> findShiftsBetweenDates(LocalTime startTime, LocalTime endTime);
    List<Shift> findShiftsByStaffIdBetweenDates(int staff_id, LocalTime startTime, LocalTime endTime);




}
