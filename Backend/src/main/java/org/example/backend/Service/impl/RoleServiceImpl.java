package org.example.backend.Service.impl;

import org.example.backend.Dao.RoleDao;
import org.example.backend.Model.entity.Role;
import org.example.backend.Service.RoleService;

public class RoleServiceImpl implements RoleService {
    public final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role getRoleById(long id) {
        Role role = roleDao.findById(id);
        if (role == null) {
            throw new IllegalArgumentException("Role not found by id.");
        }
        return role;
    }
}
