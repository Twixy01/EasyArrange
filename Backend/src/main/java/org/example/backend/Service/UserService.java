package org.example.backend.Service;

import org.example.backend.Model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    User createUser(User user);

    User updateUser(long id, User user);

    void deleteUser(long id);
    User getLoginUser(String email, String password);
}
