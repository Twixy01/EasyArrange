package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.DTO.StaffShift.StaffShiftRequest;
import org.example.backend.DTO.StaffShift.StaffShiftResponse;
import org.example.backend.DTO.StaffShift.StaffShiftResponseMapper;
import org.example.backend.Model.entity.*;
import org.example.backend.Repository.ShiftRepository;
import org.example.backend.Repository.StaffRepository;
import org.example.backend.Repository.StaffShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffShiftService {
    private final StaffResponseMapper staffResponseMapper;
    private final StaffRepository staffRepository;
    private final ShiftRepository shiftRepository;
    private StaffShiftRepository staffShiftRepository;
    private StaffShiftResponseMapper staffShiftResponseMapper;

    @Autowired
    public StaffShiftService(StaffShiftRepository staffShiftRepository, StaffShiftResponseMapper staffShiftResponseMapper, StaffResponseMapper staffResponseMapper, StaffRepository staffRepository, ShiftRepository shiftRepository) {
        this.staffShiftRepository = staffShiftRepository;
        this.staffShiftResponseMapper = staffShiftResponseMapper;
        this.staffResponseMapper = staffResponseMapper;
        this.staffRepository = staffRepository;
        this.shiftRepository = shiftRepository;
    }

    public List<StaffShiftResponse> findAll() {
        return staffShiftRepository.findAll().stream()
                .map(staffShiftResponseMapper)
                .collect(Collectors.toList());
    }

    public StaffShiftResponse findById(Long staffId, Long shiftId) {
        StaffShiftId id = new StaffShiftId();
        id.setStaffId(staffId);
        id.setShiftId(shiftId);

        Optional<StaffShift> staffShift = staffShiftRepository.findById(id);
        return staffShift.map(staffShiftResponseMapper).orElseThrow(() -> new IllegalArgumentException("StaffShift not found"));
    }

    //needs ShiftDTO !!!
    public List<Shift> findAllShiftsByStaffId(Long staffId) {
        return staffShiftRepository.findAllShiftsByStaffId(staffId);
    }

    public List<StaffResponse> findAllStaffByShiftId(Long shiftId) {
        return staffShiftRepository.findAllStaffByShiftId(shiftId).stream()
                .map(staffResponseMapper)
                .collect(Collectors.toList());
    }

    //needs ShiftDTO !!!
    public List<Shift> findAllShiftsByStaffIdBetweenShifts(Long staffId, LocalTime startShift, LocalTime endShift) {
        return staffShiftRepository.findAllShiftsByStaffIdBetweenShifts(staffId, startShift, endShift);
    }

    @Transactional
    public StaffShiftResponse create(StaffShiftRequest staffShiftRequest) {
        Staff staff = staffRepository.findById(staffShiftRequest.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + staffShiftRequest.staffId()));

        String roleName = staff.getUser().getRole().getName();
        if (!roleName.equals("STAFF")) {
            throw new IllegalArgumentException("User with id: " + staff.getUser().getId() + " is not a staff");
        }

        Shift shift = shiftRepository.findById(staffShiftRequest.shiftId())
                .orElseThrow(() -> new IllegalArgumentException("Shift not found with id: " + staffShiftRequest.shiftId()));

        StaffShift staffShift = new StaffShift();
        staffShift.setStaff(staff);
        staffShift.setShift(shift);

        staffShiftRepository.save(staffShift);

        return staffShiftResponseMapper.apply(staffShift);
    }

    //Update is not needed, because StaffShift has only 2 fields and they are both part of the primary key.
    /*@Transactional
    public StaffShift update(StaffShift staffShift) {
        return staffShiftRepository.save(staffShift);
    }*/

    @Transactional
    public void remove(Long staffId, Long shiftId) {
        StaffShiftId staffShiftId = new StaffShiftId();
        staffShiftId.setStaffId(staffId);
        staffShiftId.setShiftId(shiftId);

        staffShiftRepository.deleteById(staffShiftId);
    }
}
