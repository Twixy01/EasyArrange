package org.example.backend.Repository;

import org.example.backend.Model.entity.StaffService;
import org.example.backend.Model.entity.StaffServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffServiceRepository extends JpaRepository<StaffService, StaffServiceId> {
    @Query("SELECT ss FROM StaffService ss WHERE ss.id.staffId = :staffId")
    List<StaffService> findAllServicesByStaffId(long staffId);

    @Query("SELECT ss FROM StaffService ss WHERE ss.id.serviceId = :serviceId")
    List<StaffService> findAllStaffByServiceId(long serviceId);
}