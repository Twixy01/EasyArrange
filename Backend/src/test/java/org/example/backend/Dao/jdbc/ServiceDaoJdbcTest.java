package org.example.backend.Dao.jdbc;


import jakarta.servlet.http.HttpSession;
import org.example.backend.Dao.interfaces.RoleDao;
import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.Service;
import org.example.backend.Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDaoJdbcTest {

    Connection conn;
    UserDao model;


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
        model = new UserDaoJdbc(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    void remove_worksAsIntended() throws SQLException {
        Service service = new Service("Manikűr", 6500, 45);
        ServiceDao serviceDao = new ServiceDaoJdbc(conn);

        serviceDao.create(service);

        Service createdService = serviceDao.findServiceById(service.getId());
        assertNotNull(createdService, "Service should exist before removal");

        serviceDao.remove(createdService);

        Service deletedService = serviceDao.readServiceByName("Manikűr");
        assertNull(deletedService, "Service should no longer exist in the database after removal");
    }

    @Test
    void update_worksAsIntended() throws SQLException {
        Service service = new Service("Pedikűr", 8000, 60);
        ServiceDao serviceDao = new ServiceDaoJdbc(conn);

        serviceDao.create(service);

        Service createdService = serviceDao.findServiceById(service.getId());
        assertNotNull(createdService, "Service should exist before update");

        createdService.setPrice(8500);
        createdService.setDuration(75);
        serviceDao.update(createdService);

        Service updatedService = serviceDao.findServiceById(service.getId());
        assertNotNull(updatedService, "Updated service should still exist");
        assertEquals(8500, updatedService.getPrice(), "Price should be updated to 8500");
        assertEquals(75, updatedService.getDuration(), "Duration should be updated to 75 minutes");
        serviceDao.remove(service);
    }

   @Test
    void readServiceById_worksAsIntended() throws SQLException {
        Service service = new Service("Szempilla lifting", 12000, 90);
        ServiceDao serviceDao = new ServiceDaoJdbc(conn);

        serviceDao.create(service);

        Service foundService = serviceDao.findServiceById(service.getId());
        assertNotNull(foundService, "Service should be found by ID");
        assertEquals("Szempilla lifting", foundService.getName(), "Service name should match");
        assertEquals(12000, foundService.getPrice(), "Service price should match");
        assertEquals(90, foundService.getDuration(), "Service duration should match");
        serviceDao.remove(service);
    }
}