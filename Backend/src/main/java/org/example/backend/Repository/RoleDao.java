package org.example.backend.Repository;

import org.example.backend.Model.entity.Role;

public interface RoleDao extends Dao<Role>{
    long findRoleIdByName(String roleName);
}
