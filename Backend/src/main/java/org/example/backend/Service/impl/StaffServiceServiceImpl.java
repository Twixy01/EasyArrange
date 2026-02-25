package org.example.backend.Service.impl;

import org.example.backend.Model.entity.Service;
import org.example.backend.Service.StaffServiceService;

import java.util.List;

public class StaffServiceServiceImpl implements StaffServiceService {
    private final StaffServiceService staffServiceService;

    public StaffServiceServiceImpl(StaffServiceService staffServiceService) {
        this.staffServiceService = staffServiceService;
    }


    @Override
    public List<Service> getAllServiceByStaffId(long staffId) {
        return List.of();
    }

    @Override
    public List<Service> getAllServiceByServiceId(long serviceId) {
        return List.of();
    }
}
