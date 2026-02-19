package org.example.backend.Dao.jpa;

import org.example.backend.Dao.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoJdbcTest {

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
        model = new UserDaoJPA(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
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
            created = model.findUserById(user.getUser_id());
            assertNotNull(created, "User should be created and found in the database");

            assertEquals(user.getUser_id(), created.getUser_id());
            assertEquals(user.getEmail(), created.getEmail());
            assertEquals(user.getName(), created.getName());
            assertEquals(user.getProfilePicture(), created.getProfilePicture());
            assertEquals(user.getRoleId(), created.getRoleId());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findUserById(user.getUser_id());
        assertNull(created);
    }

    @Test
    void test_CreateMethod_ThrowException_for_ExistingEmail() throws SQLException {
        String email = "test+" + System.currentTimeMillis() + "@gfirst.com";
        User user = new User("Test3", email, "test/profile", "test123", 2);
        User created = null;
        try {
            model.create(user);
            created = model.findUserById(user.getUser_id());
            IllegalArgumentException thrownException = assertThrows(
                    IllegalArgumentException.class,
                    () -> model.create(user)
            );

            assertEquals("This user already exists", thrownException.getMessage());
        } finally {
            if (created != null) model.remove(created);
        }
        created = model.findUserById(user.getUser_id());
        assertNull(created);
    }
    @Test
    void remove_WorksAsIntended() throws SQLException {
        User user = new User("Milan", "almagmail.com", "picture_link", "4321", 3);
        UserDao userDao = new UserDaoJPA(conn);

        userDao.create(user);

        User createdUser = userDao.findUserById(user.getUser_id());
        assertNotNull(createdUser, "User should exist before removal");

        userDao.remove(createdUser);

        assertNull(userDao.findUserById(createdUser.getUser_id()), "User should be removed from DB");
    }

    @Test
    void update_WorksAsIntended() throws SQLException {
        User user = new User("Milan", "almagmail.com", "picture_link", "4321", 3);
        UserDao userDao = new UserDaoJPA(conn);

        userDao.create(user);

        User createdUser = userDao.findUserById(user.getUser_id());
        assertNotNull(createdUser, "User should exist before update");

        createdUser.setName("Updated Milan");
        userDao.update(createdUser);

        User updatedUser = userDao.findUserById(createdUser.getUser_id());
        assertNotNull(updatedUser, "Updated user should still exist");
        assertEquals("Updated Milan", updatedUser.getName(), "User name should be updated");
        userDao.remove(user);
    }

    @Test
    void read_worksAsIntended() throws SQLException {
        User user = new User("Milan", "almagmail.com", "picture_link", "4321", 3);
        UserDao userDao = new UserDaoJPA(conn);

        userDao.create(user);

        User createdUser = userDao.findUserById(user.getUser_id());
        assertNotNull(createdUser, "User should exist after creation");
        assertEquals("Milan", createdUser.getName(), "User name should match");
        assertEquals("almagmail.com", createdUser.getEmail(), "User email should match");
        userDao.remove(user);
    }
}