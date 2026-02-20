package org.example.backend.Service.impl;

import org.example.backend.Dao.RoleDao;
import org.example.backend.Service.RoleService;

public class RoleServiceImpl implements RoleService {
    public final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public String getRoleById(long id) {
        String roleName = roleDao.findRoleNameById(id);
        if (roleName == null) {
            throw new IllegalArgumentException("Role not found by id.");
        }
        return roleName;
    }
}
