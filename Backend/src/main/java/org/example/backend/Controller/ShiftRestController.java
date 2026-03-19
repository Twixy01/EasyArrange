package org.example.backend.Controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.Shift.ShiftCreateRequest;
import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shifts")
@Validated
public class ShiftRestController {

    private final ShiftService shiftService;
    private final Function<Shift, ShiftResponse> responseMapper = shift -> new ShiftResponse(
            shift.getId(), shift.getDay(), shift.getStartShift(), shift.getEndShift()
    );
    private final Function<ShiftCreateRequest, Shift> createMapper = request -> {
        Shift shift = new Shift();
        shift.setStartShift(request.startShift());
        shift.setEndShift(request.endShift());
        return shift;
    };

    @Autowired
    public ShiftRestController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public List<ShiftResponse> getAll() {
        return shiftService.findAllShiftsBetweenShifts(null, null).stream().map(responseMapper).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ShiftResponse getById(@PathVariable("id") Long id) {
        Shift shift = shiftService.findShiftById(id);
        return responseMapper.apply(shift);
    }

    @GetMapping("/start")
    public List<ShiftResponse> getByStart(@RequestParam("time") LocalTime time) {
        return shiftService.findAllShiftsByStartShift(time).stream().map(responseMapper).collect(Collectors.toList());
    }

    @GetMapping("/end")
    public List<ShiftResponse> getByEnd(@RequestParam("time") LocalTime time) {
        return shiftService.findAllShiftsByEndShift(time).stream().map(responseMapper).collect(Collectors.toList());
    }

    @PostMapping
    public ShiftResponse create(@Valid @RequestBody ShiftCreateRequest request) {
        Shift shift = createMapper.apply(request);
        Shift created = shiftService.create(shift);
        return responseMapper.apply(created);
    }

    @PutMapping("/{id}")
    public ShiftResponse update(@PathVariable("id") Long id, @Valid @RequestBody ShiftCreateRequest request) {
        Shift shift = createMapper.apply(request);
        shift.setId(id);
        Shift updated = shiftService.update(shift);
        return responseMapper.apply(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        shiftService.remove(id);
    }
}
