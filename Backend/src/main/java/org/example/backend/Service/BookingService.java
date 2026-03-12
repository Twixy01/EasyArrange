package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Booking> findBookingsBetween(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return bookingRepository.findAllBetween(start, end);
    }

    public List<Booking> findBookingsByStaffBetween(Long staffId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return bookingRepository.findAllByStaffBetween(staffId, start, end);
    }

    public List<Booking> findBookingsByCustomerBetween(Long customerId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return bookingRepository.findAllByCustomerBetween(customerId, start, end);
    }

    public List<Booking> findBookingsByStatus(org.example.backend.Model.entity.BookingStatus status) {
        return bookingRepository.findAllBookingsByStatus(status);
    }

    @Transactional
    public Booking create(@Valid Booking booking) {
        var startTime = booking.getStartDatetime().toLocalTime();
        var endTime = booking.getEndDatetime().toLocalTime();

        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        return bookingRepository.save(booking);
    }


    @Transactional
    public Booking update(@Valid Booking booking) {
        var startTime = booking.getStartDatetime().toLocalTime();
        var endTime = booking.getEndDatetime().toLocalTime();

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end time cannot be null");
        } else if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        return bookingRepository.save(booking);
    }

    @Transactional
    public void remove(Long id) {
        bookingRepository.deleteById(id);
    }

}
