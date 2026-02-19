package org.example.backend.Dao;

import java.sql.SQLException;
import java.util.List;

public interface UserDao extends Dao<User>{
    User findUser(String email, String password) throws SQLException;
    User findUserById(long userId) throws SQLException;
    List<User> findUsersByRoleName(String roleName) throws SQLException;
    List<User> findAllStaff() throws SQLException;
    List<User> findAllCustomer() throws SQLException;
    List<User> searchUsersByName(String namePart) throws SQLException;
    boolean emailExists(String email) throws SQLException;
}
