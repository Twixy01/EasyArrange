package org.example.backend.Service.impl;

import org.example.backend.RepositoryOld.UserDao;
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
        return userDao.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userDao.findById(id);
    }

    @Override
    public void createUser(User user) {
        userDao.create(user);
    }

    @Override
    public void updateUser(long id, User user) {
        userDao.update(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.remove(user);
    }

    @Override
    public User getLoginUser(String email, String password){
        if (email == null || email.isEmpty() || password == null || password.isEmpty()){
            throw new RuntimeException("Email and password must not be empty");
        }
//        validateEmailFormat(email);
//        validatePasswordStrength(password);
        User user = userDao.findUser(email, password);
        if (user == null){
            throw new IllegalArgumentException("Invalid email or password");
        }
        return user;
    }

    /*private void validatePasswordStrength(String password) {
        if (password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("Password must contain at least one digit");
        }
        if (!password.matches(".*[@#$%^&+=].*")) {
            throw new RuntimeException("Password must contain at least one special character (@#$%^&+=)");
        }
    }

    private void validateEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
        }
    }*/

}
