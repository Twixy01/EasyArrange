package org.example.backend.Dao.interfaces;

import java.sql.SQLException;

public interface RoleDao<E> extends Dao<E>{
    //READ
    long findRoleIdByName(String roleName) throws SQLException;
    String findRoleNameById(long id) throws SQLException;
}
