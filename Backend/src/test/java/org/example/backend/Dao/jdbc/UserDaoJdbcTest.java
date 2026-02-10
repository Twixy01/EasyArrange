package org.example.backend.Dao.jdbc;

import jakarta.servlet.http.HttpSession;
import org.example.backend.Dao.interfaces.RoleDao;
import org.example.backend.Dao.interfaces.UserDao;
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
        model = new UserDaoJdbc(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    void remove_WorksAsIntended() throws SQLException {
        User user = new User("Milan", "almagmail.com", "picture_link", "4321", 3);
        UserDao userDao = new UserDaoJdbc(conn);

        userDao.create(user);

        User createdUser = userDao.findUserById(user.getId());
        assertNotNull(createdUser, "User should exist before removal");

        userDao.remove(createdUser);

        assertNull(userDao.findUserById(createdUser.getId()), "User should be removed from DB");
    }

    @Test
    void update_WorksAsIntended() throws SQLException {
        User user = new User("Milan", "almagmail.com", "picture_link", "4321", 3);
        UserDao userDao = new UserDaoJdbc(conn);

        userDao.create(user);

        User createdUser = userDao.findUserById(user.getId());
        assertNotNull(createdUser, "User should exist before update");

        createdUser.setName("Updated Milan");
        userDao.update(createdUser);

        User updatedUser = userDao.findUserById(createdUser.getId());
        assertNotNull(updatedUser, "Updated user should still exist");
        assertEquals("Updated Milan", updatedUser.getName(), "User name should be updated");
        userDao.remove(user);
    }

    @Test
    void read_worksAsIntended() throws SQLException {
        User user = new User("Milan", "almagmail.com", "picture_link", "4321", 3);
        UserDao userDao = new UserDaoJdbc(conn);

        userDao.create(user);

        User createdUser = userDao.findUserById(user.getId());
        assertNotNull(createdUser, "User should exist after creation");
        assertEquals("Milan", createdUser.getName(), "User name should match");
        assertEquals("almagmail.com", createdUser.getEmail(), "User email should match");
        userDao.remove(user);
    }
}