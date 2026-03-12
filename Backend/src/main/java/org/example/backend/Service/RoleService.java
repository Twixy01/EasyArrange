package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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


    public Long findRoleIdByName(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        return role.map(Role::getId).orElseThrow(()->new RuntimeException("Role Not Found With Name : " + roleName));
    }

    public Role findRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);

        return role.orElseThrow(()->
                new IllegalArgumentException("Role not found with id: " + id));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role create(@Valid Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(@Valid Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void remove(Long id) {
        roleRepository.deleteById(id);
    }

}
