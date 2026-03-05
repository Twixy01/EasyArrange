package org.example.backend.Service;

import org.example.backend.Model.entity.Shift;

import java.time.LocalTime;
import java.util.List;

public interface ShiftService {
    List<Shift> getAllShifts();
    List<Shift> getAllShiftsByTime(LocalTime time);
    List<Shift> getShiftsBetweenTimes(LocalTime startTime, LocalTime endTime);
    List<Shift> getShiftsByStaffIdBetweenTimes(int staffId, LocalTime startTime, LocalTime endTime);

}
