package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Shift findShiftById(Long id) {
        return shiftRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Shift not found with id: " + id));
    }

    public List<Shift> findAllShiftsByStartShift(LocalTime startShift) {
        return shiftRepository.findAllShiftsByStartShift(startShift);
    }

    public List<Shift> findAllShiftsByEndShift(LocalTime endShift) {
        return shiftRepository.findAllShiftsByEndShift(endShift);
    }

    public List<Shift> findAllShiftsBetweenShifts(LocalTime startShift, LocalTime endShift) {
        return shiftRepository.findAllShiftsBetweenShifts(startShift, endShift);
    }

    @Transactional
    public Shift create(Shift shift) {
        validateShiftTimes(shift);
        if (shiftRepository.existsByDayAndStartShiftAndEndShift(
                shift.getDay(), shift.getStartShift(), shift.getEndShift())) {
            throw new IllegalArgumentException("Shift with the same day, start time, and end time already exists");
        }
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift update(Shift shift) {
        if (shift.getId() == null) {
            throw new IllegalArgumentException("Shift id is required for update");
        }

        Shift existing = shiftRepository.findById(shift.getId()).orElseThrow(() ->
                new IllegalArgumentException("Shift not found with id: " + shift.getId()));

        validateShiftTimes(shift);
        if (shiftRepository.existsByDayAndStartShiftAndEndShiftAndIdNot(
                shift.getDay(), shift.getStartShift(), shift.getEndShift(), shift.getId())) {
            throw new IllegalArgumentException("Shift with the same day, start time, and end time already exists");
        }

        existing.setDay(shift.getDay());
        existing.setStartShift(shift.getStartShift());
        existing.setEndShift(shift.getEndShift());

        return shiftRepository.save(existing);
    }

    @Transactional
    public void remove(Long id) {
        shiftRepository.deleteById(id);
    }

    private void validateShiftTimes(Shift shift) {
        if (shift.getStartShift().isAfter(shift.getEndShift())) {
            throw new IllegalArgumentException("Shift start time must be before end time");
        }
        if (shift.getStartShift().equals(shift.getEndShift())) {
            throw new IllegalArgumentException("Shift start and end times must not be the same");
        }
    }

}
