package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.backend.DTO.Booking.BookingResponse;
import org.example.backend.DTO.Booking.BookingResponseMapper;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingResponseMapper responseMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingResponseMapper responseMapper) {
        this.bookingRepository = bookingRepository;
        this.responseMapper = responseMapper;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public List<BookingResponse> findAllResponses() {
        return findAll().stream().map(responseMapper).collect(Collectors.toList());
    }

    public Booking findBookingById(long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking with id " + id + " not found"));
    }

    public BookingResponse findBookingByIdResponse(long id) {
        return responseMapper.apply(findBookingById(id));
    }

    public List<Booking> findBookingsByStaffId(Long staffId) {
        return bookingRepository.findAllByStaffId(staffId);
    }

    public List<BookingResponse> findBookingsByStaffIdResponses(Long staffId) {
        return findBookingsByStaffId(staffId).stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Booking> findBookingsByCustomerId(Long customerId) {
        return bookingRepository.findAllByCustomerId(customerId);
    }

    public List<BookingResponse> findBookingsByCustomerIdResponses(Long customerId) {
        return findBookingsByCustomerId(customerId).stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findAllBetween(start, end);
    }

    public List<BookingResponse> findBookingsBetweenResponses(LocalDateTime start, LocalDateTime end) {
        return findBookingsBetween(start, end).stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Booking> findBookingsByStatus(org.example.backend.Model.entity.BookingStatus status) {
        return bookingRepository.findAllBookingsByStatus(status);
    }

    public List<BookingResponse> findBookingsByStatusResponses(org.example.backend.Model.entity.BookingStatus status) {
        return findBookingsByStatus(status).stream().map(responseMapper).collect(Collectors.toList());
    }

    public List<Booking> findBookingsByStaffAtDatetimeAsc(Long staffId, java.time.LocalDateTime datetime) {
        return bookingRepository.findAllByOrderByStartDatetimeAsc();
    }

    public List<Booking> findBookingsByStaffAtDatetimeDesc(Long staffId, java.time.LocalDateTime datetime) {
        return bookingRepository.findAllByOrderByStartDatetimeDesc();
    }

    @Transactional
    public Booking create(@NotNull @Valid Booking booking) {
        if (booking.getStartDatetime() == null || booking.getEndDatetime() == null) {
            throw new IllegalArgumentException("Start and end datetime cannot be null");
        }

        LocalDateTime start = booking.getStartDatetime();
        LocalDateTime end = booking.getEndDatetime();

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public BookingResponse createResponse(@NotNull @Valid Booking booking) {
        Booking created = create(booking);
        return responseMapper.apply(created);
    }

    @Transactional
    public Booking update(@NotNull @Valid Booking booking) {
        if (booking.getStartDatetime() == null || booking.getEndDatetime() == null) {
            throw new IllegalArgumentException("Start and end datetime cannot be null");
        }

        LocalDateTime start = booking.getStartDatetime();
        LocalDateTime end = booking.getEndDatetime();

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }
        return bookingRepository.save(booking);
    }

    @Transactional
    public BookingResponse updateResponse(@NotNull @Valid Booking booking) {
        Booking updated = update(booking);
        return responseMapper.apply(updated);
    }

    @Transactional
    public void remove(Long id) {
        bookingRepository.deleteById(id);
    }

}
