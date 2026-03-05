package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Shift findShiftById(Long id) {
        return shiftRepository.findShiftById(id).orElseThrow(() ->
                new IllegalArgumentException("Shift not found with id: " + id));
    }

    public List<Shift> findAllShiftsByStaffId(long staffId) {
        return shiftRepository.findAllShiftsByStaffId(staffId);
    }

    public List<Shift> findAllShiftsByTime(java.time.LocalTime time) {
        return shiftRepository.findAllShiftsByTime(time);
    }

    public List<Shift> findShiftsBetweenTime(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        return shiftRepository.findShiftsBetweenTime(startTime, endTime);
    }

    public List<Shift> findShiftsByStaffIdBetweenTimes(long staff_id, java.time.LocalTime startTime, java.time.LocalTime endTime) {
        return shiftRepository.findShiftsByStaffIdBetweenTimes(staff_id, startTime, endTime);
    }

    @Transactional
    public Shift create(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift update(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Transactional
    public void delete(Long id) {
        shiftRepository.deleteById(id);
    }


}
