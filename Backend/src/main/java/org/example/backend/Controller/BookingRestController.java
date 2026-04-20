package org.example.backend.Controller;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @GetMapping("/staff/{staffId}/overlaps")
    public List<BookingResponse> getOverlaps(
            @PathVariable Long staffId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return bookingService.findOverlappingBookings(staffId, start, end);
    }

    @PostMapping("/create")
    public BookingResponse createBooking(@Valid @RequestBody BookingCreateRequest request) {
        return bookingService.create(request);
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(@PathVariable("id") Long id, @Valid @RequestBody BookingUpdateRequest request) {
        return bookingService.update(id, request);
    }

    @PostMapping("/cancel/{id}")
    public void cancelBooking(@PathVariable("id") Long id) {
        bookingService.cancel(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        bookingService.remove(id);
    }
}
