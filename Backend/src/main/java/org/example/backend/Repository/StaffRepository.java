package org.example.backend.Repository;

import org.example.backend.Model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE s.user.id = :userId")
    Optional<Staff> findStaffByUserId(@Param("userId") Long id);
    boolean existsByUserId(Long userId);
}
