package org.example.backend.Controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.Booking.*;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Model.entity.BookingStatus;
import org.example.backend.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingRestController {

    private final BookingService bookingService;
    private final BookingResponseMapper responseMapper;
    private final BookingUpdateRequestMapper updateMapper;

    @Autowired
    public BookingRestController(BookingService bookingService, BookingResponseMapper responseMapper, BookingUpdateRequestMapper updateMapper) {
        this.bookingService = bookingService;
        this.responseMapper = responseMapper;
        this.updateMapper = updateMapper;
    }

    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.findAllResponses();
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable("id") Long id) {
        return bookingService.findBookingByIdResponse(id);
    }

    @GetMapping("/staff/{staffId}")
    public List<BookingResponse> getByStaff(@PathVariable("staffId") Long staffId) {
        return bookingService.findBookingsByStaffIdResponses(staffId);
    }

    @GetMapping("/customer/{customerId}")
    public List<BookingResponse> getByCustomer(@PathVariable("customerId") Long customerId) {
        return bookingService.findBookingsByCustomerIdResponses(customerId);
    }

    @GetMapping("/between")
    public List<BookingResponse> getBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return bookingService.findBookingsBetweenResponses(start, end);
    }

    @GetMapping("/status")
    public List<BookingResponse> getByStatus(@RequestParam("status") String status) {
        BookingStatus bookingStatus = BookingStatus.valueOf(status);
        return bookingService.findBookingsByStatusResponses(bookingStatus);
    }

    @PostMapping
    public BookingResponse create(@Valid @RequestBody BookingCreateRequest request) {
        return bookingService.create(request);
    }

    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable("id") Long id, @Valid @RequestBody BookingUpdateRequest request) {
        Booking booking = updateMapper.apply(request);
        booking.setId(id);
        return bookingService.updateResponse(booking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        bookingService.remove(id);
    }
}
