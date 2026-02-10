package org.example.backend.Dao.jdbc;

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

class UserDaoJdbcTest {
    Connection connection;
    UserDao model;

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
        model = new UserDaoJdbc(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void createUser_whenEmailIsUnique_shouldCreateUser() throws SQLException {
        String email = "test+" + System.currentTimeMillis() + "@gfirst.com";
        User user = new User("Test2", email, "test/profile", "test123", 2);
        ;
        User created = null;
        try {
            model.create(user);
            created = model.findUserById(user.getId());
            assertNotNull(created, "User should be created and found in the database");

            assertEquals(user.getId(), created.getId());
            assertEquals(user.getEmail(), created.getEmail());
            assertEquals(user.getName(), created.getName());
            assertEquals(user.getProfilePicture(), created.getProfilePicture());
            assertEquals(user.getRoleId(), created.getRoleId());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findUserById(user.getId());
        assertNull(created);
    }

    @Test
    void test_CreateMethod_ThrowException_for_ExistingEmail() throws SQLException {
        String email = "test+" + System.currentTimeMillis() + "@gfirst.com";
        User user = new User("Test3", email, "test/profile", "test123", 2);
        User created = null;
        try {
            model.create(user);
            created = model.findUserById(user.getId());
            IllegalArgumentException thrownException = assertThrows(
                    IllegalArgumentException.class,
                    () -> model.create(user)
            );

            assertEquals("This user already exists", thrownException.getMessage());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findUserById(user.getId());
        assertNull(created);
    }
    /*@Test
    void update() {
    }

    @Test
    void remove() {
    }

    @Test
    void findAll() {
    }*/
    /*@Test
    void findUserTest() {

    }

    @Test
    void findUsersByRoleName() {

    }

    @Test
    void findAllStaff() {
    }

    @Test
    void findAllCustomer() {
    }

    @Test
    void searchUsersByName() {
    }

    @Test
    void findUserById() {
    }*/
}
