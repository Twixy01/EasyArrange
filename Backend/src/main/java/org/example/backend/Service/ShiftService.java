package org.example.backend.Service;

import org.example.backend.Model.entity.Shift;

import java.time.LocalTime;
import java.util.List;

public interface ShiftService {

    List<Shift> getAllShifts();
    List<Shift> getShiftsByDate(LocalTime time);
    List<Shift> getShiftsBetweenDates(LocalTime startTime, LocalTime endTime);
    List<Shift> getShiftsByStaffIdBetweenDates(int staff_id, LocalTime startTime, LocalTime endTime);

}
