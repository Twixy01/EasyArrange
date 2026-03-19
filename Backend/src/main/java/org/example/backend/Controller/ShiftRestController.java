package org.example.backend.Controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.Shift.ShiftCreateRequest;
import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.Service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@Validated
public class ShiftRestController {

    private final ShiftService shiftService;

    @Autowired
    public ShiftRestController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public List<ShiftResponse> getAll() {
        return shiftService.findAllShiftsBetweenShiftsResponse(null, null);
    }

    @GetMapping("/{id}")
    public ShiftResponse getById(@PathVariable("id") Long id) {
        return shiftService.findShiftByIdResponse(id);
    }

    @GetMapping("/start")
    public List<ShiftResponse> getByStart(@RequestParam("time") LocalTime time) {
        return shiftService.findAllShiftsByStartShiftResponse(time);
    }

    @GetMapping("/end")
    public List<ShiftResponse> getByEnd(@RequestParam("time") LocalTime time) {
        return shiftService.findAllShiftsByEndShiftResponse(time);
    }

    @PostMapping
    public ShiftResponse create(@Valid @RequestBody ShiftCreateRequest request) {
        return shiftService.create(request);
    }

    @PutMapping("/{id}")
    public ShiftResponse update(@PathVariable("id") Long id, @Valid @RequestBody ShiftCreateRequest request) {
        return shiftService.updateResponse(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        shiftService.remove(id);
    }
}
