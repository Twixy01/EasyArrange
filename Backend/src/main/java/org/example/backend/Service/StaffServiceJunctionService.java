package org.example.backend.Service;

import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.Service.ServiceResponseMapper;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.DTO.StaffService.StaffServiceRequest;
import org.example.backend.DTO.StaffService.StaffServiceResponse;
import org.example.backend.DTO.StaffService.StaffServiceResponseMapper;
import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffServiceId;
import org.example.backend.Model.entity.StaffServiceJunction;
import org.example.backend.Repository.ServiceRepository;
import org.example.backend.Repository.StaffRepository;
import org.example.backend.Repository.StaffServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class StaffServiceJunctionService {
    private final ServiceResponseMapper serviceResponseMapper;
    private final StaffResponseMapper staffResponseMapper;
    private final StaffServiceRepository staffServiceRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final StaffServiceResponseMapper staffServiceResponseMapper;

    @Autowired
    public StaffServiceJunctionService(StaffServiceRepository staffServiceRepository, ServiceResponseMapper serviceResponseMapper, StaffResponseMapper staffResponseMapper, StaffRepository staffRepository, ServiceRepository serviceRepository, StaffServiceResponseMapper staffServiceResponseMapper) {
        this.staffServiceRepository = staffServiceRepository;
        this.serviceResponseMapper = serviceResponseMapper;
        this.staffResponseMapper = staffResponseMapper;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
        this.staffServiceResponseMapper = staffServiceResponseMapper;
    }

    public List<StaffServiceResponse> findAll() {
        return staffServiceRepository.findAll().stream()
                .map(staffServiceResponseMapper)
                .collect(Collectors.toList());
    }

    public StaffServiceResponse findById(Long staffId, Long serviceId) {
        StaffServiceId staffServiceId = new StaffServiceId();

        staffServiceId.setStaffId(staffId);
        staffServiceId.setServiceId(serviceId);

        Optional<StaffServiceJunction> staffService = staffServiceRepository.findById(staffServiceId);
        return staffService.map(staffServiceResponseMapper).orElseThrow(() -> new IllegalArgumentException("StaffServiceJunction not found"));
    }

    public List<ServiceResponse> findAllServicesByStaffId(Long staffId) {
        return staffServiceRepository.findAllServicesByStaffId(staffId).stream()
                .map(serviceResponseMapper)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> findAllStaffByServiceId(Long serviceId) {
        return staffServiceRepository.findAllStaffByServiceId(serviceId).stream()
                .map(staffResponseMapper)
                .collect(Collectors.toList());
    }

    public StaffServiceResponse create(StaffServiceRequest staffServiceRequest) {
        StaffServiceJunction junctionEntity = new StaffServiceJunction();

        Staff staff = staffRepository.findById(staffServiceRequest.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + staffServiceRequest.staffId()));
        Service service = serviceRepository.findById(staffServiceRequest.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + staffServiceRequest.serviceId()));

        junctionEntity.setStaff(staff);
        junctionEntity.setService(service);

        staffServiceRepository.save(junctionEntity);
        return staffServiceResponseMapper.apply(junctionEntity);
    }

    // No update method is provided as the junction table typically represents a many-to-many relationship and doesn't have additional attributes to update.
    /*public StaffServiceJunction update(StaffServiceJunction staffService) {
        return staffServiceRepository.save(staffService);
    }*/

    public void remove(Long staffId, Long serviceId) {
        StaffServiceId staffServiceId = new StaffServiceId();

        staffServiceId.setStaffId(staffId);
        staffServiceId.setServiceId(serviceId);

        staffServiceRepository.deleteById(staffServiceId);
    }
}
