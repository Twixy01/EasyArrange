package org.example.backend.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.StaffService.StaffServiceRequest;
import org.example.backend.DTO.StaffService.StaffServiceResponse;
import org.example.backend.Service.StaffServiceJunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-services")
@CrossOrigin
@Validated
public class StaffServiceRestController {
    private final StaffServiceJunctionService staffServiceJunctionService;

    @Autowired
    public StaffServiceRestController(StaffServiceJunctionService staffServiceJunctionService) {
        this.staffServiceJunctionService = staffServiceJunctionService;
    }

    @GetMapping
    public List<StaffServiceResponse> getAllStaffServices() {
        return staffServiceJunctionService.findAll();
    }

    @GetMapping("/{staffId}/{serviceId}")
    public StaffServiceResponse getStaffServiceById(@PathVariable @Positive Long staffId, @PathVariable @Positive Long serviceId) {
        return staffServiceJunctionService.findById(staffId,serviceId);
    }

    @GetMapping("/staff/{staffId}")
    public List<ServiceResponse> getServicesByStaffId(@PathVariable @Positive Long staffId) {
        return staffServiceJunctionService.findAllServicesByStaffId(staffId);
    }

    @GetMapping("/service/{serviceId}")
    public List<StaffResponse> getStaffByServiceId(@PathVariable @Positive Long serviceId) {
        return staffServiceJunctionService.findAllStaffByServiceId(serviceId);
    }

    @PostMapping("/create")
    public StaffServiceResponse createStaffService(@Valid @RequestBody StaffServiceRequest staffServiceRequest){
        return staffServiceJunctionService.create(staffServiceRequest);
    }

    @DeleteMapping("{staffId}/{serviceId}")
    public void deleteStaffService(@PathVariable @Positive Long staffId, @PathVariable @Positive Long serviceId) {
        staffServiceJunctionService.remove(staffId, serviceId);
    }

}
