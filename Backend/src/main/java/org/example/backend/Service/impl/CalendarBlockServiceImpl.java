package org.example.backend.Service.impl;

import org.example.backend.Repository.CalendarBlockDao;
import org.example.backend.Model.entity.CalendarBlock;
import org.example.backend.Service.CalendarBlockService;

import java.time.LocalDateTime;
import java.util.List;

public class CalendarBlockServiceImpl implements CalendarBlockService {
    private final CalendarBlockDao calendarBlockDao;

    public CalendarBlockServiceImpl(CalendarBlockDao calendarBlockDao) {
        this.calendarBlockDao = calendarBlockDao;
    }

    @Override
    public List<CalendarBlock> getCalendarBlocksByStaffId(int staffId) {
        return calendarBlockDao.findCalendarBlocksByStaffId(staffId);
    }

    @Override
    public List<CalendarBlock> getCalendarBlocksBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return calendarBlockDao.findCalendarBlocksBetween(startDate, endDate);
    }

    @Override
    public List<CalendarBlock> getCalendarBlocksByStaffIdBetween(int staffId, LocalDateTime startDate, LocalDateTime endDate) {
        return calendarBlockDao.findCalendarBlocksByStaffBetween(staffId, startDate, endDate);
    }
}
