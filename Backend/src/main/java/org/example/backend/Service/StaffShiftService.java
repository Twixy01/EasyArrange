package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.*;
import org.example.backend.Repository.StaffShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class StaffShiftService {
    private StaffShiftRepository staffShiftRepository;

    @Autowired
    public StaffShiftService(StaffShiftRepository staffShiftRepository) {
        this.staffShiftRepository = staffShiftRepository;
    }

    public List<StaffShift> findAll() {
        return staffShiftRepository.findAll();
    }

    public StaffShift findById(StaffShiftId id) {
        Optional<StaffShift> staffShift = staffShiftRepository.findById(id);
        return staffShift.orElseThrow(() -> new IllegalArgumentException("StaffShift not found"));
    }

    public List<Shift> findAllShiftsByStaffId(Long staffId) {
        return staffShiftRepository.findAllShiftsByStaffId(staffId);
    }

    public List<Staff> findAllStaffByShiftId(Long shiftId) {
        return staffShiftRepository.findAllStaffByShiftId(shiftId);
    }

    public List<Shift> findAllShiftsByStaffIdBetweenShifts(Long staffId, LocalTime startShift, LocalTime endShift) {
        return staffShiftRepository.findAllShiftsByStaffIdBetweenShifts(staffId, startShift, endShift);
    }

    @Transactional
    public StaffShift create(StaffShift staffShift) {
        return staffShiftRepository.save(staffShift);
    }

    @Transactional
    public StaffShift update(StaffShift staffShift) {
        return staffShiftRepository.save(staffShift);
    }

    @Transactional
    public void remove(StaffShiftId staffShiftId) {
        staffShiftRepository.deleteById(staffShiftId);
    }
}
