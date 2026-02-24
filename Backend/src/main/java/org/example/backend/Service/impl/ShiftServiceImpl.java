package org.example.backend.Service.impl;

import org.example.backend.Dao.ShiftDao;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Service.ShiftService;

import java.time.LocalTime;
import java.util.List;

public class ShiftServiceImpl implements ShiftService {
    private final ShiftDao shiftDao;

    public ShiftServiceImpl(ShiftDao shiftDao) {
        this.shiftDao = shiftDao;
    }

    @Override
    public List<Shift> getAllShifts() {
        return List.of();
    }

    @Override
    public List<Shift> getShiftsByDate(LocalTime time) {
        return List.of();
    }

    @Override
    public List<Shift> getShiftsBetweenDates(LocalTime startTime, LocalTime endTime) {
        return List.of();
    }

    @Override
    public List<Shift> getShiftsByStaffIdBetweenDates(int staff_id, LocalTime startTime, LocalTime endTime) {
        return List.of();
    }

}
