package org.example.backend.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.backend.DTO.CalendarBlock.CalendarBlockRequest;
import org.example.backend.DTO.CalendarBlock.CalendarBlockResponse;
import org.example.backend.Service.CalendarBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar-blocks")
@CrossOrigin
@Validated
public class CalendarBlockRestController {

    private final CalendarBlockService calendarBlockService;

    @Autowired
    public CalendarBlockRestController(CalendarBlockService calendarBlockService) {
        this.calendarBlockService = calendarBlockService;
    }

    @GetMapping
    public List<CalendarBlockResponse> getCalendarBlocks() {
        return calendarBlockService.findAllByOrderByStartDatetimeAsc();
    }

    @GetMapping("/{calendarBlockId}")
    public CalendarBlockResponse getCalendarBlockById(@PathVariable("calendarBlockId") @Positive Long calendarBlockId) {
        return calendarBlockService.findCalendarBlockById(calendarBlockId);
    }

    @GetMapping("/staff/{staffId}")
    public List<CalendarBlockResponse> getCalendarBlocksByStaffId(@PathVariable("staffId") @Positive Long staffId) {
        return calendarBlockService.findCalendarBlocksByStaffId(staffId);
    }

    @GetMapping("/between")
    public List<CalendarBlockResponse> getCalendarBlocksBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return calendarBlockService.findCalendarBlocksBetween(start, end);
    }

    @GetMapping("/staff/{staffId}/between")
    public List<CalendarBlockResponse> getCalendarBlocksByStaffBetween(
            @PathVariable("staffId") @Positive Long staffId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return calendarBlockService.findCalendarBlocksByStaffBetween(staffId, start, end);
    }

    @PostMapping("/create")
    public CalendarBlockResponse createCalendarBlock(@Valid @RequestBody CalendarBlockRequest calendarBlockDto) {
        return calendarBlockService.create(calendarBlockDto);
    }

    @PutMapping("/{calendarBlockId}")
    public CalendarBlockResponse updateCalendarBlock(
            @PathVariable("calendarBlockId") @Positive Long calendarBlockId,
            @Valid @RequestBody CalendarBlockRequest calendarBlockDto) {
        return calendarBlockService.update(calendarBlockId, calendarBlockDto);
    }

    @DeleteMapping("/{calendarBlockId}")
    public void deleteCalendarBlock(@PathVariable("calendarBlockId") @Positive Long calendarBlockId) {
        calendarBlockService.remove(calendarBlockId);
    }
}
