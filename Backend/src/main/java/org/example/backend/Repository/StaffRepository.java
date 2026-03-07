package org.example.backend.Repository;

import org.example.backend.Model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE s.user.id = :id")
    Optional<Staff> findStaffByUserId(long id);
}
