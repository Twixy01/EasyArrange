package org.example.backend.Service;

import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffService;
import org.example.backend.Model.entity.StaffServiceId;
import org.example.backend.Repository.StaffServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.backend.Model.entity.Service;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class StaffServiceService {
    private StaffServiceRepository staffServiceRepository;

    @Autowired
    public StaffServiceService(StaffServiceRepository staffServiceRepository) {
        this.staffServiceRepository = staffServiceRepository;
    }

    public List<StaffService> findAll() {
        return staffServiceRepository.findAll();
    }

    public StaffService findById(StaffServiceId id) {
        Optional<StaffService> staffService = staffServiceRepository.findById(id);
        return staffService.orElseThrow(() -> new IllegalArgumentException("StaffService not found"));
    }

    public List<Service> findAllServicesByStaffId(Long staffId) {
        return staffServiceRepository.findAllServicesByStaffId(staffId);
    }

    public List<Staff> findAllStaffByServiceId(Long serviceId) {
        return staffServiceRepository.findAllStaffByServiceId(serviceId);
    }

    public StaffService create(StaffService staffService) {
        return staffServiceRepository.save(staffService);
    }

    public StaffService update(StaffService staffService) {
        return staffServiceRepository.save(staffService);
    }

    public void remove(StaffServiceId staffServiceId) {
        staffServiceRepository.deleteById(staffServiceId);
    }
}
