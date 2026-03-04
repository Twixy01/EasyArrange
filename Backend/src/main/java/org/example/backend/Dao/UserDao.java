package org.example.backend.Dao;

import org.example.backend.Model.entity.User;

import java.util.List;

public interface UserDao extends Dao<User>{
    User findUser(String email, String password);
    List<User> findUsersByRoleName(String roleName);
    List<User> findAllStaff();
    List<User> findAllCustomer();
    List<User> searchUsersByName(String namePart);
    boolean emailExists(String email);
}
