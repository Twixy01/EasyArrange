package org.example.backend.Repository;

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
    Optional<User> findUserByEmail(String email);

    List<User> findUsersByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role.name = 'CUSTOMER'")
    List<User> findAllCustomers();

    @Query("SELECT u FROM User u WHERE u.role.name = 'STAFF'")
    List<User> findAllStaff();

    @Query("SELECT u FROM User u WHERE u.name LIKE %:namePart%")
    List<User> findUsersByName(@Param("namePart") String namePart);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean emailExists(@Param("email") String email);
}
