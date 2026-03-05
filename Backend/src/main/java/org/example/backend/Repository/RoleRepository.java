package org.example.backend.Repository;

import org.example.backend.Model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    long findRoleIdByName(String roleName);
    Optional<Role> findRoleById(Long id);
    List<Role> findAll();
}
