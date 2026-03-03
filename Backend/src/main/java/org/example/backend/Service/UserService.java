package org.example.backend.Service;

import org.example.backend.Model.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(long userId);
    void createUser(User user);
    void updateUser(long userId, User user);
    void deleteUser(User user);
    User getLoginUser(String email, String password);
}
