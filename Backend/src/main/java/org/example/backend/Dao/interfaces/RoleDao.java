package org.example.backend.Dao.interfaces;

import org.example.backend.Entities.Role;

import java.sql.SQLException;

public interface RoleDao extends Dao<Role>{
    long findRoleIdByName(String roleName) throws SQLException;
    String findRoleNameById(long id) throws SQLException;
}
