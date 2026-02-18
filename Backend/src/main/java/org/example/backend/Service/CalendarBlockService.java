package org.example.backend.Service;

import org.example.backend.Dao.interfaces.CalendarBlockDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Dao.jdbc.CalendarBlockDaoJdbc;
import org.example.backend.Dao.jdbc.UserDaoJdbc;
import org.example.backend.Entities.CalendarBlock;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CalendarBlockService {
    private final CalendarBlockDao calendarBlockDao;
    private final UserDao userDao;

    public CalendarBlockService(Connection connection) {
        this.calendarBlockDao = new CalendarBlockDaoJdbc(connection);
        this.userDao = new UserDaoJdbc(connection);
    }

    public CalendarBlock createBlock(CalendarBlock block) throws SQLException {
        if (block == null) throw new IllegalArgumentException("CalendarBlock cannot be null");
        LocalDateTime start = block.getStartDatetime();
        LocalDateTime end = block.getEndDatetime();
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("Invalid start/end times");
        }

        if (userDao.findUserById(block.getStaffId()) == null) {
            throw new IllegalArgumentException("Staff user not found");
        }

        List<CalendarBlock> overlaps = calendarBlockDao.findCalendarBlocksByStaffBetween(block.getStaffId(), start, end);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Calendar block overlaps existing block");
        }

        calendarBlockDao.create(block);
        return block;
    }

    public void updateBlock(CalendarBlock block) throws SQLException {
        if (block == null) throw new IllegalArgumentException("CalendarBlock cannot be null");
        if (calendarBlockDao.findCalendarBlockById(block.getId()) == null) throw new IllegalArgumentException("Block not found");
        calendarBlockDao.update(block);
    }

    public void deleteBlock(long id) throws SQLException {
        CalendarBlock existing = calendarBlockDao.findCalendarBlockById(id);
        if (existing == null) throw new IllegalArgumentException("Block not found");
        calendarBlockDao.remove(existing);
    }

    public CalendarBlock getBlockById(long id) throws SQLException {
        return calendarBlockDao.findCalendarBlockById(id);
    }

    public List<CalendarBlock> listStaffBlocks(long staffId) throws SQLException {
        return calendarBlockDao.findCalendarBlocksByStaffId(staffId);
    }
}

