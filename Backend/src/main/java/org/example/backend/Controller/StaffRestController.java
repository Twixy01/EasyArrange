package org.example.backend.Controller;

import org.example.backend.DTO.Staff.StaffRequest;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffRestController {

    private final StaffService staffService;
    private final StaffResponseMapper staffResponseMapper;

    @Autowired
    public StaffRestController(StaffService staffService, StaffResponseMapper staffResponseMapper) {
        this.staffService = staffService;
        this.staffResponseMapper = staffResponseMapper;
    }

    @GetMapping
    public List<StaffResponse> getStaff() {
        return staffService.findAll();
    }

    @GetMapping("/{staffId}")
    public StaffResponse getStaffById(@PathVariable("staffId") Long staffId) {
        return staffService.findById(staffId);
    }

    @GetMapping("/user/{userId}")
    public StaffResponse getStaffByUserId(@PathVariable("userId") Long userId) {
        return staffService.findStaffByUserId(userId);
    }

    @PostMapping("/register")
    public StaffResponse addStaff(@RequestBody StaffRequest staffDto) {
        return staffService.create(staffDto);
    }

    //update staff is not needed as staff only has userId which cannot be updated,
    // if we want to update the userId we need to delete the staff and create a new one with the new userId

    @DeleteMapping("/{staffId}")
    public void deleteStaff(@PathVariable("staffId") Long staffId) {
        staffService.remove(staffId);
    }
}
