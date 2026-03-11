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

    public List<Booking> findBookingsByStatus(org.example.backend.Model.entity.BookingStatus status) {
        return bookingRepository.findAllBookingsByStatus(status);
    }

    public List<Booking> findBookingsByStaffAtDatetimeAsc(Long staffId, java.time.LocalDateTime datetime) {
        return bookingRepository.findAllByOrderByStartDatetimeAsc();
    }

    public List<Booking> findBookingsByStaffAtDatetimeDesc(Long staffId, java.time.LocalDateTime datetime) {
        return bookingRepository.findAllByOrderByStartDatetimeDesc();
    }

    @Transactional
    public Booking create(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking must not be null");
        }

        if (booking.getId() != null && bookingRepository.existsById(booking.getId())) {
            throw new IllegalArgumentException("Booking with id " + booking.getId() + " already exists");
        }


        if (booking.getStaff() == null || booking.getStaff().getId() == null) {
            throw new IllegalArgumentException("Booking must have a staff assigned");
        }
        if (booking.getCustomer() == null || booking.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Booking must have a customer assigned");
        }
        if (booking.getService() == null || booking.getService().getId() == null) {
            throw new IllegalArgumentException("Booking must have a service assigned");
        }
        if (booking.getStartDatetime() == null || booking.getEndDatetime() == null) {
            throw new IllegalArgumentException("Booking must have start and end datetimes");
        }
        if (booking.getEndDatetime().isBefore(booking.getStartDatetime()) || booking.getEndDatetime().isEqual(booking.getStartDatetime())) {
            throw new IllegalArgumentException("Booking end must be after start");
        }


        List<Booking> overlapping = bookingRepository.findAllByStaffBetween(
                booking.getStaff().getId(), booking.getStartDatetime(), booking.getEndDatetime());
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Staff already has a booking in the given time range");
        }

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
