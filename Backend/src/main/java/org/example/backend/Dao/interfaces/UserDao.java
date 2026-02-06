package org.example.backend.Dao.interfaces;

import org.example.backend.Entities.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserDao<E> extends Dao<E>{
    //Create


    //Read

    E findUser(String email, String password) throws SQLException;
    E findUserById(long userId) throws SQLException;
    List<E> findUsersByRoleName(String roleName) throws SQLException;
    List<E> findAllStaff() throws SQLException;
    List<E> findAllCustomer() throws SQLException;
    List<E> searchUsersByName(String namePart) throws SQLException;
    void createUser (User user) throws SQLException;
    //Update

    //Delete
}
