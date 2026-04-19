package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.DTO.Shift.ShiftResponseMapper;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.DTO.StaffShift.ShiftUpdateRequest;
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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffShiftService {
    private final StaffResponseMapper staffResponseMapper;
    private final StaffRepository staffRepository;
    private final ShiftRepository shiftRepository;
    private StaffShiftRepository staffShiftRepository;
    private StaffShiftResponseMapper staffShiftResponseMapper;
    private ShiftResponseMapper shiftResponseMapper;


    @Autowired
    public StaffShiftService(StaffShiftRepository staffShiftRepository, StaffShiftResponseMapper staffShiftResponseMapper, StaffResponseMapper staffResponseMapper, StaffRepository staffRepository, ShiftRepository shiftRepository, ShiftResponseMapper shiftResponseMapper) {
        this.staffShiftRepository = staffShiftRepository;
        this.staffShiftResponseMapper = staffShiftResponseMapper;
        this.staffResponseMapper = staffResponseMapper;
        this.staffRepository = staffRepository;
        this.shiftRepository = shiftRepository;
        this.shiftResponseMapper = shiftResponseMapper;
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

    public List<ShiftResponse> findAllShiftsByStaffId(Long staffId) {
        return staffShiftRepository.findAllShiftsByStaffId(staffId).stream()
                .map(shiftResponseMapper)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> findAllStaffByShiftId(Long shiftId) {
        return staffShiftRepository.findAllStaffByShiftId(shiftId).stream()
                .map(staffResponseMapper)
                .collect(Collectors.toList());
    }

    public List<ShiftResponse> findAllShiftsByStaffIdBetweenShifts(Long staffId, LocalTime startShift, LocalTime endShift) {
        return staffShiftRepository.findAllShiftsByStaffIdBetweenShifts(staffId, startShift, endShift).stream()
                .map(shiftResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public StaffShiftResponse updateShiftForStaffDay(ShiftUpdateRequest request) {
        Long staffId = request.staffId();
        ShiftDay day = parseShiftDay(request.day());
        LocalTime startShift = request.startShift();
        LocalTime endShift = request.endShift();

        validateShiftTimes(startShift, endShift);

        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + staffId));

        String roleName = staff.getUser().getRole().getName();
        if (!canManageStaffShifts(roleName)) {
            throw new IllegalArgumentException("User with id: " + staff.getUser().getId() + " cannot have shift updates");
        }

        Shift targetShift = shiftRepository.findByDayAndStartShiftAndEndShift(day, startShift, endShift)
                .orElseGet(()-> {
                    Shift shift = new Shift();
                    shift.setDay(day);
                    shift.setStartShift(startShift);
                    shift.setEndShift(endShift);
                    return shiftRepository.save(shift);
                });

        Optional<StaffShift> existingStaffShiftOpt = staffShiftRepository.findStaffShiftByStaffAndShiftDay(staffId, day);

        if (existingStaffShiftOpt.isPresent()) {
            StaffShift existingStaffShift = existingStaffShiftOpt.get();

            if (existingStaffShift.getShift().getId().equals(targetShift.getId())) {
                return staffShiftResponseMapper.apply(existingStaffShift);
            }

            staffShiftRepository.delete(existingStaffShift);
        }

        StaffShift staffShift = new StaffShift();
        staffShift.setStaff(staff);
        staffShift.setShift(targetShift);
        staffShift = staffShiftRepository.save(staffShift);

        return staffShiftResponseMapper.apply(staffShift);
    }

    @Transactional
    public StaffShiftResponse create(StaffShiftRequest staffShiftRequest) {
        Staff staff = staffRepository.findById(staffShiftRequest.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + staffShiftRequest.staffId()));

        String roleName = staff.getUser().getRole().getName();
        if (!canManageStaffShifts(roleName)) {
            throw new IllegalArgumentException("User with id: " + staff.getUser().getId() + " cannot have shifts");
        }

        Shift shift = shiftRepository.findById(staffShiftRequest.shiftId())
                .orElseThrow(() -> new IllegalArgumentException("Shift not found with id: " + staffShiftRequest.shiftId()));


        staffShiftRepository.findStaffShiftByStaffAndShiftDay(staff.getId(), shift.getDay())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Staff with id: " + staff.getId() + " already has a shift on day: " + shift.getDay()
                    );
                });

        StaffShift staffShift = new StaffShift();
        staffShift.setStaff(staff);
        staffShift.setShift(shift);

        StaffShift saved = staffShiftRepository.save(staffShift);

        return staffShiftResponseMapper.apply(saved);
    }


    @Transactional
    public void remove(Long staffId, Long shiftId) {
        StaffShiftId staffShiftId = new StaffShiftId();
        staffShiftId.setStaffId(staffId);
        staffShiftId.setShiftId(shiftId);

        staffShiftRepository.deleteById(staffShiftId);
    }

    private void validateShiftTimes(LocalTime startShift, LocalTime endShift) {
        if (!startShift.isBefore(endShift)) {
            throw new IllegalArgumentException("Shift start time must be before end time");
        }

        if (startShift.plusHours(1).isAfter(endShift)) {
            throw new IllegalArgumentException("Shift must be at least 1 hour long");
        }
    }

    private ShiftDay parseShiftDay(String rawDay) {
        try {
            return ShiftDay.valueOf(rawDay.trim().toUpperCase());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Day must be one of: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY");
        }
    }

    private boolean canManageStaffShifts(String roleName) {
        if (roleName == null) {
            return false;
        }

        String normalizedRole = roleName.trim().toUpperCase(Locale.ROOT);
        return normalizedRole.equals("STAFF") || normalizedRole.equals("ADMIN");
    }
}
