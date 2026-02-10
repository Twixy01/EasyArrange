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
    void successfullyCreatedBooking() throws SQLException {
        Booking booking = new Booking(
                3,
                4,
                LocalDateTime.of(2026, 2, 10, 13, 0, 0),
                LocalDateTime.of(2026, 2, 10, 13, 40, 0),
                1
        );
        Booking created = null;
        try {
            model.create(booking);
            created = model.findBookingById(booking.getId());
            assertNotNull(created, "Booking should be created and found in the database");

            assertEquals(booking.getId(), created.getId());
            assertEquals(booking.getStaffId(), created.getStaffId());
            assertEquals(booking.getCustomerId(), created.getCustomerId());
            assertEquals(booking.getStartDatetime(), created.getStartDatetime());
            assertEquals(booking.getEndDatetime(), created.getEndDatetime());
            assertEquals(booking.getServiceId(), created.getServiceId());

        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findBookingById(booking.getId());
        assertNull(created);
    }

    @Test
    void createMethod_ThrowException_for_ExistingBooking_Or_SameStaffWithSamePeriod() throws SQLException {
        Booking original = new Booking(
                3,
                4,
                LocalDateTime.of(2011, 1, 10, 10, 0, 0),
                LocalDateTime.of(2011, 1, 10, 10, 40, 0),
                1
        );
        Booking created = null;
        try {
            model.create(original);
            created = model.findBookingById(original.getId());

            IllegalArgumentException thrownException = assertThrows(
                    IllegalArgumentException.class,
                    () -> model.create(original)
            );
            assertEquals("Datetime collision, when trying to create a booking.", thrownException.getMessage());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findBookingById(original.getId());
        assertNull(created);
    }

    @Test
    void createMethod_ThrowException_for_BookingsCollisionFromRight() throws SQLException {
        Booking original = new Booking(
                3,
                4,
                LocalDateTime.of(2011, 1, 10, 10, 0, 0),
                LocalDateTime.of(2011, 1, 10, 10, 40, 0),
                1
        );
        Booking created = null;
        try {
            model.create(original);
            created = model.findBookingById(original.getId());
            Booking booking2 = new Booking(
                    3,
                    10,
                    LocalDateTime.of(2011, 1, 10, 10, 20, 0),
                    LocalDateTime.of(2011, 1, 10, 11, 0, 0),
                    2
            );
            IllegalArgumentException thrownException = assertThrows(
                    IllegalArgumentException.class,
                    () -> model.create(booking2)
            );
            assertEquals("Datetime collision, when trying to create a booking.", thrownException.getMessage());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findBookingById(original.getId());
        assertNull(created);
    }

    @Test
    void createMethod_ThrowException_for_BookingsCollisionFromLeft() throws SQLException {
        Booking original = new Booking(
                3,
                4,
                LocalDateTime.of(2011, 1, 10, 10, 0, 0),
                LocalDateTime.of(2011, 1, 10, 10, 40, 0),
                1
        );
        Booking created = null;
        try {
            model.create(original);
            created = model.findBookingById(original.getId());
            Booking booking2 = new Booking(
                    3,
                    10,
                    LocalDateTime.of(2011, 1, 10, 9, 30, 0),
                    LocalDateTime.of(2011, 1, 10, 9, 59, 0),
                    2
            );
            IllegalArgumentException thrownException = assertThrows(
                    IllegalArgumentException.class,
                    () -> model.create(booking2)
            );
            assertEquals("Datetime collision, when trying to create a booking.", thrownException.getMessage());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findBookingById(original.getId());
        assertNull(created);
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