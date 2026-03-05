package org.example.backend.Service.impl;

import org.example.backend.RepositoryOld.ShiftDao;
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
        return shiftDao.findAll();
    }

    @Override
    public List<Shift> getAllShiftsByTime(LocalTime time) {
        return shiftDao.findAllShiftsByTime(time);
    }

    @Override
    public List<Shift> getShiftsBetweenTimes(LocalTime startTime, LocalTime endTime) {
        return shiftDao.findShiftsBetweenTime(startTime, endTime);
    }

    @Override
    public List<Shift> getShiftsByStaffIdBetweenTimes(int staffId, LocalTime startTime, LocalTime endTime) {
        return shiftDao.findShiftsByStaffIdBetweenTimes(staffId, startTime, endTime);
    }
}
