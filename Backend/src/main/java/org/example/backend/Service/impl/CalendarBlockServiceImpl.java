package org.example.backend.Service.impl;

import org.example.backend.Dao.CalendarBlockDao;
import org.example.backend.Model.entity.CalendarBlock;

import java.time.LocalDateTime;
import java.util.List;

public class CalendarBlockServiceImpl implements CalendarBlockDao {
    private final CalendarBlock calendarBlock;

    public CalendarBlockServiceImpl(CalendarBlock calendarBlock) {
        this.calendarBlock = calendarBlock;
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksByStaffId(long staffId) {
        return List.of();
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) {
        return List.of();
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) {
        return List.of();
    }

    @Override
    public CalendarBlock findById(long id) {
        return null;
    }

    @Override
    public void create(CalendarBlock object) {

    }

    @Override
    public void update(CalendarBlock object) {

    }

    @Override
    public void remove(CalendarBlock object) {

    }

    @Override
    public List<CalendarBlock> findAll() {
        return List.of();
    }
}
