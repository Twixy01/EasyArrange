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

    List<Shift> findAllShiftsByStartShift(LocalTime startShift){
        return shiftRepository.findAllShiftsByStartShift(startShift);
    }

    List<Shift> findAllShiftsByEndShift(LocalTime endShift){
        return shiftRepository.findAllShiftsByEndShift(endShift);
    }

    List<Shift> findAllShiftsBetweenShifts(LocalTime startShift, LocalTime endShift){
        return shiftRepository.findAllShiftsBetweenShifts(startShift, endShift);
    }

    @Transactional
    public Shift create(Shift shift) {
        if (shift == null) {
            throw new IllegalArgumentException("Shift must not be null");
        }
        if (shift.getId() != null && shiftRepository.existsById(shift.getId())) {
            throw new IllegalArgumentException("Shift with id " + shift.getId() + " already exists");
        }
        if (shift.getStartShift() == null || shift.getEndShift() == null) {
            throw new IllegalArgumentException("Shift start and end times must not be null");
        }
        if (shift.getStartShift().isAfter(shift.getEndShift())) {
            throw new IllegalArgumentException("Shift start time must be before end time");
        }
        if (shift.getStartShift().equals(shift.getEndShift())) {
            throw new IllegalArgumentException("Shift start and end times must not be the same");
        }

        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift update(Shift shift) {
        if (shift == null) {
            throw new IllegalArgumentException("Shift must not be null");
        }

        if (shift.getId() == null) {
            throw new IllegalArgumentException("Shift id must be provided for update");
        }

        Shift existing = shiftRepository.findById(shift.getId()).orElseThrow(() ->
                new IllegalArgumentException("Shift not found with id: " + shift.getId()));

        if (shift.getStartShift() == null || shift.getEndShift() == null) {
            throw new IllegalArgumentException("Shift start and end times must not be null");
        }
        if (shift.getStartShift().isAfter(shift.getEndShift())) {
            throw new IllegalArgumentException("Shift start time must be before end time");
        }
        if (shift.getStartShift().equals(shift.getEndShift())) {
            throw new IllegalArgumentException("Shift start and end times must not be the same");
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


}
