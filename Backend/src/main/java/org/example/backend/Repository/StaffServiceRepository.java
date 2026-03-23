package org.example.backend.Repository;

import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffServiceJunction;
import org.example.backend.Model.entity.StaffServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffServiceRepository extends JpaRepository<StaffServiceJunction, StaffServiceId> {
    @Query("SELECT ss.service FROM StaffServiceJunction ss WHERE ss.id.staffId = :staffId")
    List<Service> findAllServicesByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT ss.staff FROM StaffServiceJunction ss WHERE ss.id.serviceId = :serviceId")
    List<Staff> findAllStaffByServiceId(@Param("serviceId") Long serviceId);
}