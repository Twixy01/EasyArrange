package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.StaffShift;
import org.example.backend.Model.entity.StaffShiftId;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.StaffShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    List<StaffShift> findAllShiftsByStaffId(long staffId) {
        return staffShiftRepository.findAllShiftsByStaffId(staffId);
    }

    List<StaffShift> findAllStaffByShiftId(long shiftId) {
        return staffShiftRepository.findAllStaffByShiftId(shiftId);
    }

    @Transactional
    public void remove(StaffShift staffShift) {
        staffShiftRepository.delete(staffShift);
    }

    @Transactional
    public StaffShift update(StaffShift staffShift) {
        return staffShiftRepository.save(staffShift);
    }

    @Transactional
    public StaffShift create(StaffShift staffShift) {
        return staffShiftRepository.save(staffShift);
    }
}
