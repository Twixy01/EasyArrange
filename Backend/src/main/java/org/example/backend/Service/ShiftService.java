package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift update(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Transactional
    public void remove(Long id) {
        shiftRepository.deleteById(id);
    }


}
