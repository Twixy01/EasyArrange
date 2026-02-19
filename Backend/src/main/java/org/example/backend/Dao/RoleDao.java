package org.example.backend.Dao;

import javax.management.relation.Role;
import java.sql.SQLException;

public interface RoleDao extends Dao<Role>{
    long findRoleIdByName(String roleName) throws SQLException;
    String findRoleNameById(long id) throws SQLException;
}
