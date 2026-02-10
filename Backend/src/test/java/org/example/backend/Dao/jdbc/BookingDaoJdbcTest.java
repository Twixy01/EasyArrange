package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.BookingDao;
import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.Booking;
import org.example.backend.Entities.Service;
import org.example.backend.Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingDaoJdbcTest {

    Connection conn;
    BookingDao model;


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
        model = new BookingDaoJdbc(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    void remove_worksAsIntended() throws SQLException {

        UserDao userDao = new UserDaoJdbc(conn);
        ServiceDao serviceDao = new ServiceDaoJdbc(conn);

        User staff = new User("Test Staff", "staff.booking@test.com", "pic", "pass", 2);
        User customer = new User("Test Customer", "customer.booking@test.com", "pic", "pass", 3);
        userDao.create(staff);
        userDao.create(customer);
        User createdStaff = userDao.findUser(staff.getEmail(), staff.getPassword());
        User createdCustomer = userDao.findUser(customer.getEmail(), customer.getPassword());

        Service service = new Service("Test Booking Service", 6500, 60);
        serviceDao.create(service);
        Service createdService = serviceDao.readServiceByName(service.getName());


        Booking booking = new Booking(createdStaff.getId(), createdCustomer.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), createdService.getId());
        model.create(booking);

        Booking createdBooking = model.findBookingById(booking.getId());
        assertNotNull(createdBooking, "Booking should exist before removal");


        model.remove(createdBooking);

        assertNull(model.findBookingById(booking.getId()), "Booking should be removed from DB");


        serviceDao.remove(createdService);
        userDao.remove(createdStaff);
        userDao.remove(createdCustomer);
    }
}