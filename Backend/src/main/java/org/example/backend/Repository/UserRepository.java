package org.example.backend.Repository;

import org.example.backend.DTO.UserDTO;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndPassword(String email, String password);

    List<User> findUsersByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role.name = 'customer'")
    List<User> findAllCustomer();

    @Query("SELECT u FROM User u WHERE u.role.name = 'staff'")
    List<User> findAllStaff();

    @Query("SELECT u FROM User u WHERE u.name LIKE %:namePart%")
    List<User> findUsersByName(@Param("namePart") String namePart);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean emailExists(@Param("email") String email);
}
