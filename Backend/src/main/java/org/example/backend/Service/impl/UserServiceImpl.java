package org.example.backend.Service.impl;

import org.example.backend.Dao.UserDao;
import org.example.backend.Model.entity.User;
import org.example.backend.Service.UserService;


import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(long id, User user) {
        return null;
    }

    @Override
    public void deleteUser(long id) {

    }

    @Override
    public void checkLogin() {

    }

}
