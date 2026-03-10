package org.example.backend.Service;

import jakarta.transaction.Transactional;
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

    @Transactional
    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking update(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Transactional
    public void remove(Long id) {
        bookingRepository.deleteById(id);
    }

}
