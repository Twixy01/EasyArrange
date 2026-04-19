package org.example.backend.Controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.Booking.*;
import org.example.backend.DTO.TimeSlot.AvailableSlotResponse;
import org.example.backend.Model.entity.BookingStatus;
import org.example.backend.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
@Validated
public class BookingRestController {

    private final BookingService bookingService;

    @Autowired
    public BookingRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable("id") Long id) {
        return bookingService.findBookingById(id);
    }

    @GetMapping("/staff/{staffId}")
    public List<BookingResponse> getByStaff(@PathVariable("staffId") Long staffId) {
        return bookingService.findBookingsByStaffId(staffId);
    }

    @GetMapping("/customer/{customerId}")
    public List<BookingResponse> getByCustomer(@PathVariable("customerId") Long customerId) {
        try {
            return bookingService.findBookingsByCustomerId(customerId);
        } catch (Exception ex) {
            // defensive: if something goes wrong while computing/updating bookings, don't fail the GET
            System.err.println("Error while fetching bookings for customer " + customerId + ": " + ex.getMessage());
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/between")
    public List<BookingResponse> getBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return bookingService.findBookingsBetween(start, end);
    }

    @GetMapping("/staff/{staffId}/available-slots")
    public List<AvailableSlotResponse> getAvailableSlots(
            @PathVariable Long staffId,
            @RequestParam LocalDate selectedDate,
            @RequestParam Long serviceId
    ) {
        return bookingService.getAvailableSlots(staffId, selectedDate, serviceId);
    }

    @GetMapping("/status")
    public List<BookingResponse> getByStatus(@RequestParam("status") String status) {
        BookingStatus bookingStatus = BookingStatus.valueOf(status);
        return bookingService.findBookingsByStatus(bookingStatus);
    }

    @PostMapping("/create")
    public BookingResponse create(@Valid @RequestBody BookingCreateRequest request) {
        return bookingService.create(request);
    }

    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable("id") Long id, @Valid @RequestBody BookingUpdateRequest request) {
        return bookingService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        bookingService.remove(id);
    }

    @DeleteMapping("/{id}/hard")
    public void hardDelete(@PathVariable("id") Long id) {
        bookingService.hardRemove(id);
    }
}
