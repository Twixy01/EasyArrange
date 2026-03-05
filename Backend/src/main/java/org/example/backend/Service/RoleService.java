package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.Role;
import org.example.backend.Repository.RoleRepository;
import org.example.backend.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public long FindRoleIdByName(String roleName) {
        return roleRepository.findRoleIdByName(roleName);
    }

    public Role findRoleById(Long id) {
        Optional<Role> role = roleRepository.findRoleById(id);

        return role.orElseThrow(()->
                new IllegalArgumentException("Role not found with id: " + id));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public boolean delete(Long id) {
        roleRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Role role) {
        return roleRepository.save(role);
    }


}
