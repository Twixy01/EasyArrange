package org.example.backend.Service;

import org.example.backend.Model.entity.StaffService;
import org.example.backend.Model.entity.StaffServiceId;
import org.example.backend.Repository.StaffServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

    List<StaffService> findAllServicesByStaffId(long staffId) {
        return staffServiceRepository.findAllServicesByStaffId(staffId);
    }

    List<StaffService> findAllStaffByServiceId(long serviceId) {
        return staffServiceRepository.findAllStaffByServiceId(serviceId);
    }
}
