package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.Service;
import org.example.backend.Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDaoJdbcTest {
    Connection connection;
    ServiceDao model;

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
        model = new ServiceDaoJdbc(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void successfullyCreatedService() throws SQLException {
        Service service = new Service("Test Service", 100, 60);
        Service created = null;
        try {
            model.create(service);
            created = model.findServiceById(service.getId());
            assertNotNull(created, "Service should be created and found in the database");

            assertEquals(service.getId(), created.getId());
            assertEquals(service.getName(), created.getName());
            assertEquals(service.getPrice(), created.getPrice());
            assertEquals(service.getDuration(), created.getDuration());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findServiceById(service.getId());
        assertNull(created);
    }
    @Test
    void throwsExceptionWhenCreatingDuplicateService() throws SQLException {
        Service service = new Service("Test Service", 100, 60);
        Service created = null;
        try {
            model.create(service);
            created = model.findServiceById(service.getId());
            IllegalArgumentException thrownException = assertThrows(
                    IllegalArgumentException.class,
                    () -> model.create(service)
            );

            assertEquals("Service already exists.", thrownException.getMessage());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findServiceById(service.getId());
        assertNull(created);
    }
}