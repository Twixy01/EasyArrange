package org.example.backend.Service;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    User createUser(User user);

    User updateUser(long id, User user);

    void deleteUser(long id);

}
