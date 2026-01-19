package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.User;

import java.sql.Connection;

public class UserDaoJdbc extends JdbcConnection implements UserDao<User> {
    protected UserDaoJdbc(Connection connection) {
        super(connection);
    }
}
