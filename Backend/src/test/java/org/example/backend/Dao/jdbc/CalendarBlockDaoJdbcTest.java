package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.CalendarBlockDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.Booking;
import org.example.backend.Entities.CalendarBlock;
import org.example.backend.Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarBlockDaoJdbcTest {

    Connection conn;
    CalendarBlockDao model;


    @BeforeEach
    void setUp() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/test_easyarrange", "root", ""
        );
        model = new CalendarBlockDaoJdbc(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    void successfullyCreatedBlock() throws SQLException {
        CalendarBlock block = new CalendarBlock(
                LocalDateTime.of(2026, 5, 10, 11, 0, 0),
                LocalDateTime.of(2026, 5, 10, 11, 40, 0),
                4
        );
        CalendarBlock created = null;
        try {
            model.create(block);
            created = model.findCalendarBlockById(block.getId());
            assertNotNull(created, "CalendarBlock should be created and found in the database");

            assertEquals(block.getId(), created.getId());
            assertEquals(block.getStartDatetime(), created.getStartDatetime());
            assertEquals(block.getEndDatetime(), created.getEndDatetime());
            assertEquals(block.getStaffId(), created.getStaffId());

        } finally{
            if (created != null) model.remove(created);
        }
        created = model.findCalendarBlockById(block.getId());
        assertNull(created);
    @Test
    void remove_worksAsIntended() throws SQLException {

        UserDao userDao = new UserDaoJdbc(conn);
        String unique = String.valueOf(System.currentTimeMillis());
        User staff = new User("CB Staff " + unique, "staff.cb." + unique + "@test.com", "pic", "pass", 2);
        userDao.create(staff);
        User createdStaff = userDao.findUser(staff.getEmail(), staff.getPassword());


        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);
        CalendarBlock block = new CalendarBlock(0, start, end, createdStaff.getId());
        model.create(block);


        List<CalendarBlock> blocks = model.findCalendarBlocksByStaffId(createdStaff.getId());
        assertFalse(blocks.isEmpty(), "CalendarBlock should be created.");
        CalendarBlock createdBlock = blocks.get(0);
        assertNotNull(model.findCalendarBlockById(createdBlock.getId()), "CalendarBlock should exist before removal");


        model.remove(createdBlock);

        assertNull(model.findCalendarBlockById(createdBlock.getId()), "CalendarBlock should be removed from DB");

        userDao.remove(createdStaff);
    }
}