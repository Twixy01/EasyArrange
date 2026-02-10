package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.CalendarBlockDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.Booking;
import org.example.backend.Entities.CalendarBlock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CalendarBlockDaoJdbcTest {
    Connection connection;
    CalendarBlockDao model;

    @BeforeEach
    void setUp() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/test_easyarrange", "root", ""
        );
        model = new CalendarBlockDaoJdbc(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
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
    }
}