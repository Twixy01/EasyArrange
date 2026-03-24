package org.example.backend.Controller;

import jakarta.validation.constraints.Positive;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.StaffShift.StaffShiftRequest;
import org.example.backend.DTO.StaffShift.StaffShiftResponse;
import org.example.backend.Service.StaffShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-shift")
@Validated
public class StaffShiftRestController {
    private final StaffShiftService staffShiftService;

    @Autowired
    public StaffShiftRestController(StaffShiftService staffShiftService) {
        this.staffShiftService = staffShiftService;
    }

    @GetMapping
    public List<StaffShiftResponse> getAllStaffShifts() {
        return staffShiftService.findAll();
    }

    @GetMapping("/{staffId}/{shiftId}")
    public StaffShiftResponse getStaffShiftById(
            @PathVariable("staffId") Long staffId, @PathVariable("shiftId") @Positive Long shiftId
    ) {
        return staffShiftService.findById(staffId, shiftId);
    }

    //needs ShiftDTO !!!
    /*@GetMapping("/staff/{staffId}")
    public List<ShiftResponse> getShiftsByStaffId(@PathVariable("staffId") @Positive Long staffId) {
        return staffShiftService.findAllShiftsByStaffId(staffId);
    }*/

    @GetMapping("/shift/{shiftId}")
    public List<StaffResponse> getStaffByShiftId(@PathVariable("shiftId") @Positive Long shiftId) {
        return staffShiftService.findAllStaffByShiftId(shiftId);
    }

    @PostMapping("/create")
    public StaffShiftResponse createStaffShift(@RequestBody StaffShiftRequest staffShiftRequest) {
        return staffShiftService.create(staffShiftRequest);
    }

    @DeleteMapping("/{staffId}/{shiftId}")
    public void deleteStaffShift(@PathVariable("staffId") Long staffId, @PathVariable("shiftId") @Positive Long shiftId) {
        staffShiftService.remove(staffId, shiftId);
    }
}
