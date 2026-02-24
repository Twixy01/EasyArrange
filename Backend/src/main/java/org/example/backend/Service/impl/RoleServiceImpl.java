package org.example.backend.Service.impl;

import org.example.backend.Dao.RoleDao;
import org.example.backend.Service.RoleService;

import javax.management.relation.Role;

public class RoleServiceImpl implements RoleService {
    public final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role getRoleById(int id) {
        String roleName = roleDao.findById(id);
        if (roleName == null) {
            throw new IllegalArgumentException("Role not found by id.");
        }
        return roleName;
    }
    
}
