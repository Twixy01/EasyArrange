package org.example.backend.Dao;

import org.example.backend.Model.entity.Role;

public interface RoleDao extends Dao<Role>{
    long findRoleIdByName(String roleName);
    String findRoleNameById(long id);
}
