package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findBookingById(long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking with id " + id + " not found"));
    }

    public List<Booking> findBookingsByStaffId(Long staffId) {
        return bookingRepository.findAllByStaffId(staffId);
    }

    public List<Booking> findBookingsByCustomerId(Long customerId) {
        return bookingRepository.findAllByCustomerId(customerId);
    }

    public List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findAllBetween(start, end);
    }

    public List<Booking> findBookingsByStaffBetween(Long staffId, LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findAllByStaffBetween(staffId, start, end);
    }

    public List<Booking> findBookingsByCustomerBetween(Long customerId, LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findAllByCustomerBetween(customerId, start, end);
    }

    public List<Booking> findBookingsByStatus(org.example.backend.Model.entity.BookingStatus status) {
        return bookingRepository.findAllBookingsByStatus(status);
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
    public void remove(Long id) {
        bookingRepository.deleteById(id);
    }

}
