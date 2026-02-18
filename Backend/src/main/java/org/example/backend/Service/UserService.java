package org.example.backend.Service;

import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Dao.jdbc.UserDaoJdbc;
import org.example.backend.Entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(Connection connection) {
        this.userDao = new UserDaoJdbc(connection);
    }


    public User registerUser(User user) throws SQLException {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) throw new IllegalArgumentException("Email is required");

        if (userDao.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("This user already exists");
        }

        userDao.create(user);
        return user;
    }


    public User authenticate(String email, String password) throws SQLException {
        if (email == null || password == null) return null;
        return userDao.findUser(email, password);
    }


    public void updateUser(User user) throws SQLException {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (userDao.findUserById(user.getId()) == null) throw new IllegalArgumentException("User not found");
        userDao.update(user);
    }


    public void deleteUser(long id) throws SQLException {
        User existing = userDao.findUserById(id);
        if (existing == null) throw new IllegalArgumentException("User not found");
        userDao.remove(existing);
    }


    public User getUserById(long id) throws SQLException {
        return userDao.findUserById(id);
    }


    public List<User> listAllUsers() throws SQLException {
        return userDao.findAll();
    }
}

