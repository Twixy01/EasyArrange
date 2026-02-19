package org.example.backend.Dao.jpa;

import org.example.backend.Dao.CalendarBlockDao;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarBlockDaoJPA implements CalendarBlockDao {
    private final SessionFactory sessionFactory;

    public CalendarBlockDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//
//    @Override
//    public CalendarBlock findCalendarBlockById(long id) throws SQLException {
//        PreparedStatement calendarBlockById = connection.prepareStatement("SELECT * FROM calendar_block WHERE id = ?");
//        calendarBlockById.setLong(1,id);
//        ResultSet rs = calendarBlockById.executeQuery();
//
//        if (rs.next()){
//            return new CalendarBlock(
//                    rs.getLong("id"),
//                    rs.getTimestamp("start_datetime").toLocalDateTime(),
//                    rs.getTimestamp("end_datetime").toLocalDateTime(),
//                    rs.getLong("staff_id")
//            );
//        }
//        return null;
//    }
//
//    @Override
//    public List<CalendarBlock> findCalendarBlocksByStaffId(long staffId) throws SQLException {
//        List<CalendarBlock> blocks = new ArrayList<>();
//        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM calendar_block WHERE staff_id = ?");
//        blockByStaffId.setLong(1,staffId);
//        ResultSet rs = blockByStaffId.executeQuery();
//
//        while (rs.next()){
//            blocks.add(new CalendarBlock(
//                    rs.getLong("id"),
//                    rs.getTimestamp("start_datetime").toLocalDateTime(),
//                    rs.getTimestamp("end_datetime").toLocalDateTime(),
//                    rs.getLong("staff_id")
//            ));
//        }
//        return blocks;
//    }
//
//    @Override
//    public List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
//        List<CalendarBlock> blocks = new ArrayList<>();
//        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM calendar_block WHERE start_datetime >= ? AND end_datetime <= ?");
//        blockByStaffId.setTimestamp(1, Timestamp.valueOf(start));
//        blockByStaffId.setTimestamp(2, Timestamp.valueOf(end));
//        ResultSet rs = blockByStaffId.executeQuery();
//
//        while (rs.next()){
//            blocks.add(new CalendarBlock(
//                    rs.getLong("id"),
//                    rs.getTimestamp("start_datetime").toLocalDateTime(),
//                    rs.getTimestamp("end_datetime").toLocalDateTime(),
//                    rs.getLong("staff_id")
//            ));
//        }
//        return blocks;
//    }
//
//    @Override
//    public List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) throws SQLException {
//        List<CalendarBlock> blocks = new ArrayList<>();
//        PreparedStatement blockByStaffId = connection.prepareStatement("SELECT * FROM calendar_block WHERE staff_id = ? AND start_datetime >= ? AND end_datetime <= ?");
//        blockByStaffId.setLong(1, staffId);
//        blockByStaffId.setTimestamp(2, Timestamp.valueOf(start));
//        blockByStaffId.setTimestamp(3, Timestamp.valueOf(end));
//        ResultSet rs = blockByStaffId.executeQuery();
//
//        while (rs.next()){
//            blocks.add(new CalendarBlock(
//                    rs.getLong("id"),
//                    rs.getTimestamp("start_datetime").toLocalDateTime(),
//                    rs.getTimestamp("end_datetime").toLocalDateTime(),
//                    rs.getLong("staff_id")
//            ));
//        }
//        return blocks;
//    }
//
//    @Override
//    public void create(CalendarBlock object) throws SQLException {
//        String sql = "INSERT INTO calendar_block (start_datetime, end_datetime, staff_id) VALUES (?, ?, ?);";
//        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//        stmt.setTimestamp(1, Timestamp.valueOf(object.getStartDatetime()));
//        stmt.setTimestamp(2, Timestamp.valueOf(object.getEndDatetime()));
//        stmt.setLong(3, object.getStaffId());
//
//        stmt.executeUpdate();
//        ResultSet rs = stmt.getGeneratedKeys();
//        if (rs.next()) {
//            object.setId(rs.getLong(1));
//        }
//    }
//
//    @Override
//    public void update(CalendarBlock calendarBlock) throws SQLException {
//        String sql = "UPDATE calendar_block SET start_datetime = ?, end_datetime = ?, staff_id = ? WHERE id = ?;";
//        PreparedStatement update = connection.prepareStatement(sql);
//        update.setTimestamp(1,Timestamp.valueOf(calendarBlock.getStartDatetime()));
//        update.setTimestamp(2,Timestamp.valueOf(calendarBlock.getEndDatetime()));
//        update.setLong(3, calendarBlock.getStaffId());
//        update.setLong(4, calendarBlock.getId());
//
//        update.executeUpdate();
//    }
//
//    @Override
//    public void remove(CalendarBlock object) throws SQLException {
//        String sql = "DELETE FROM calendar_block WHERE id = ?;";
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        stmt.setLong(1, object.getId());
//        stmt.executeUpdate();
//
//    }
//
//    @Override
//    public List<CalendarBlock> findAll() throws SQLException{
//        List<CalendarBlock> blocks = new ArrayList<>();
//        Statement findAll = connection.createStatement();
//        ResultSet rs = findAll.executeQuery("SELECT * FROM calendar_block");
//        while (rs.next()){
//            blocks.add(new CalendarBlock(
//                    rs.getLong("id"),
//                    rs.getTimestamp("start_datetime").toLocalDateTime(),
//                    rs.getTimestamp("end_datetime").toLocalDateTime(),
//                    rs.getLong("staff_id")
//            ));
//        }
//        return blocks;
//    }
}
