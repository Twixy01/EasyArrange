package org.example.backend.Dao;

import javax.management.relation.Role;

public interface RoleDao extends Dao<Role>{
    long findRoleIdByName(String roleName);
    String findRoleNameById(long id);
}
