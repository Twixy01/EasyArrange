package org.example.backend.Repository;

import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffService;
import org.example.backend.Model.entity.StaffServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffServiceRepository extends JpaRepository<StaffService, StaffServiceId> {
    @Query("SELECT ss.service FROM StaffService ss WHERE ss.id.staffId = :staffId")
    List<Service> findAllServicesByStaffId(Long staffId);

    @Query("SELECT ss.staff FROM StaffService ss WHERE ss.id.serviceId = :serviceId")
    List<Staff> findAllStaffByServiceId(Long serviceId);
}