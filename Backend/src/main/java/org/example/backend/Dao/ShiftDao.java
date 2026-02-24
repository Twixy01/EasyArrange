package org.example.backend.Dao;

import org.example.backend.Model.entity.Shift;

import java.util.List;

public interface ShiftDao extends Dao<Shift> {
    List<Shift> getAllShiftsByStaffId(int staff_id);
    List<Shift> getAllShiftsByDate(String date);
    List<Shift> findShiftsBetweenDates(String startDate, String endDate);




}
