package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.Shift.ShiftCreateRequest;
import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.DTO.Shift.ShiftCreateRequestMapper;
import org.example.backend.DTO.Shift.ShiftResponseMapper;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Model.entity.ShiftDay;
import org.example.backend.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftCreateRequestMapper createMapper;
    private final ShiftResponseMapper responseMapper;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository, ShiftCreateRequestMapper createMapper, ShiftResponseMapper responseMapper) {
        this.shiftRepository = shiftRepository;
        this.createMapper = createMapper;
        this.responseMapper = responseMapper;
    }

    public Shift findShiftById(Long id) {
        return shiftRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Shift not found with id: " + id));
    }

    public ShiftResponse findShiftByIdResponse(Long id) {
        return responseMapper.apply(findShiftById(id));
    }

    public List<ShiftResponse> findAll() {
        return shiftRepository.findAll().stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Shift> findAllShiftsByStartShift(LocalTime startShift) {
        return shiftRepository.findAllShiftsByStartShift(startShift);
    }

    public List<ShiftResponse> findAllShiftsByStartShiftResponse(LocalTime startShift) {
        return findAllShiftsByStartShift(startShift).stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Shift> findAllShiftsByEndShift(LocalTime endShift) {
        return shiftRepository.findAllShiftsByEndShift(endShift);
    }

    public List<ShiftResponse> findAllShiftsByEndShiftResponse(LocalTime endShift) {
        return findAllShiftsByEndShift(endShift).stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Shift> findAllShiftsBetweenShifts(LocalTime startShift, LocalTime endShift) {
        return shiftRepository.findAllShiftsBetweenShifts(startShift, endShift);
    }

    public List<ShiftResponse> findAllShiftsBetweenShiftsResponse(LocalTime startShift, LocalTime endShift) {
        return findAllShiftsBetweenShifts(startShift, endShift).stream().map(responseMapper).collect(Collectors.toList());
    }

    @Transactional
    public ShiftResponse create(ShiftCreateRequest request) {
        // map request to entity
        Shift shift = createMapper.apply(request);

        validateShiftTimes(shift);
        if (shiftRepository.existsByDayAndStartShiftAndEndShift(
                shift.getDay(), shift.getStartShift(), shift.getEndShift())) {
            throw new IllegalArgumentException("Shift with the same day, start time, and end time already exists");
        }

        shift.setDay(ShiftDay.valueOf(request.day()));

        Shift saved = shiftRepository.save(shift);
        return responseMapper.apply(saved);
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
    public ShiftResponse updateResponse(Long id, ShiftCreateRequest request) {
        Shift shift = createMapper.apply(request);
        shift.setId(id);
        Shift updated = update(shift);
        return responseMapper.apply(updated);
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
