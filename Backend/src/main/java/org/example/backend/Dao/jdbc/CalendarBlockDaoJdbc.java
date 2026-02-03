package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.CalendarBlockDao;
import org.example.backend.Entities.CalendarBlock;
import org.example.backend.Entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarBlockDaoJdbc extends JdbcConnection implements CalendarBlockDao<CalendarBlock> {
    public CalendarBlockDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public CalendarBlock findCalendarBlockById(long id) throws SQLException {
        PreparedStatement calendarBlockById = connection.prepareStatement("SELECT * FROM calendar_block WHERE id = ?");
        calendarBlockById.setLong(1,id);
        ResultSet rs = calendarBlockById.executeQuery();

        if (rs.next()){
            return new CalendarBlock(
                    rs.getLong("id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("staff_id")
            );
        }
        return null;
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksByStaffId(long staffId) throws SQLException {
        List<CalendarBlock> blocks = new ArrayList<>();
        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM calendar_block WHERE staff_id = ?");
        blockByStaffId.setLong(1,staffId);
        ResultSet rs = blockByStaffId.executeQuery();

        while (rs.next()){
            blocks.add(new CalendarBlock(
                    rs.getLong("id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("staff_id")
            ));
        }
        return blocks;
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        List<CalendarBlock> blocks = new ArrayList<>();
        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM calendar_block WHERE start_datetime >= ? AND end_datetime <= ?");
        blockByStaffId.setTimestamp(1, Timestamp.valueOf(start));
        blockByStaffId.setTimestamp(2, Timestamp.valueOf(end));
        ResultSet rs = blockByStaffId.executeQuery();

        while (rs.next()){
            blocks.add(new CalendarBlock(
                    rs.getLong("id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("staff_id")
            ));
        }
        return blocks;
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) throws SQLException {
        List<CalendarBlock> blocks = new ArrayList<>();
        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM calendar_block WHERE staff_id = ? AND start_datetime >= ? AND end_datetime <= ?");
        blockByStaffId.setLong(1, staffId);
        blockByStaffId.setTimestamp(2, Timestamp.valueOf(start));
        blockByStaffId.setTimestamp(3, Timestamp.valueOf(end));
        ResultSet rs = blockByStaffId.executeQuery();

        while (rs.next()){
            blocks.add(new CalendarBlock(
                    rs.getLong("id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("staff_id")
            ));
        }
        return blocks;
    }

    @Override
    public List<CalendarBlock> findAll() throws SQLException{
        List<CalendarBlock> blocks = new ArrayList<>();
        Statement findAll = connection.createStatement();
        ResultSet rs = findAll.executeQuery("SELECT * FROM calendar_block");
        while (rs.next()){
            blocks.add(new CalendarBlock(
                    rs.getLong("id"),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getTimestamp("end_datetime").toLocalDateTime(),
                    rs.getLong("staff_id")
            ));
        }
        return blocks;
    }
}
