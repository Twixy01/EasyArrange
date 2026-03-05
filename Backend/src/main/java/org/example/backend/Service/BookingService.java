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


    public List<Booking> findBookingsByStaffId(long staffId) {
        return bookingRepository.findBookingsByStaffId(staffId);
    }

    public List<Booking> findBookingsByCustomerId(long customerId) {
        return bookingRepository.findBookingsByCustomerId(customerId);
    }

    public List<Booking> findBookingsBetween(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return bookingRepository.findBookingsBetween(start, end);
    }

    public List<Booking> findBookingsByStaffBetween(long staffId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return bookingRepository.findBookingsByStaffBetween(staffId, start, end);
    }

    public List<Booking> findBookingsByCustomerBetween(long customerId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return bookingRepository.findBookingsByCustomerBetween(customerId, start, end);
    }

    public  List<Booking> findAll() {
        return bookingRepository.findAll();
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
    public boolean delete(Long id) {
        bookingRepository.deleteById(id);
        return true;
    }

}
