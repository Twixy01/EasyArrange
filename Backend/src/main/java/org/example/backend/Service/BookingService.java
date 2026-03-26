package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.backend.DTO.Booking.*;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Model.entity.BookingStatus;
import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.BookingRepository;
import org.example.backend.Repository.ServiceRepository;
import org.example.backend.Repository.StaffRepository;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@org.springframework.stereotype.Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingResponseMapper responseMapper;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final BookingCreateRequestMapper bookingCreateRequestMapper;
    private final BookingUpdateRequestMapper bookingUpdateRequestMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          BookingResponseMapper responseMapper,
                          StaffRepository staffRepository,
                          UserRepository userRepository,
                          ServiceRepository serviceRepository, BookingCreateRequestMapper bookingCreateRequestMapper, BookingUpdateRequestMapper bookingUpdateRequestMapper) {
        this.bookingRepository = bookingRepository;
        this.responseMapper = responseMapper;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.bookingCreateRequestMapper = bookingCreateRequestMapper;
        this.bookingUpdateRequestMapper = bookingUpdateRequestMapper;
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
    public BookingResponse create(@NotNull @Valid BookingCreateRequest bookingRequest) {
        if (bookingRequest.startDateTime() == null || bookingRequest.endDateTime() == null) {
            throw new IllegalArgumentException("Start and end datetime cannot be null");
        }

        LocalDateTime start = bookingRequest.startDateTime();
        LocalDateTime end = bookingRequest.endDateTime();

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }

        Booking booking = bookingCreateRequestMapper.apply(bookingRequest);

        Staff staff = staffRepository.findById(bookingRequest.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff with id " + bookingRequest.staffId() + " not found"));
        booking.setStaff(staff);

        User customer = userRepository.findById(bookingRequest.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with id " + bookingRequest.customerId() + " not found"));
        booking.setCustomer(customer);

        Service service = serviceRepository.findById(bookingRequest.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service with id " + bookingRequest.serviceId() + " not found"));
        booking.setService(service);

        booking.setStatus(BookingStatus.valueOf(bookingRequest.status()));

        bookingRepository.save(booking);

        return responseMapper.apply(booking);
    }

    @Transactional
    public Booking update(@Valid Long id, BookingUpdateRequest bookingRequest) {

        LocalDateTime start = bookingRequest.startDateTime();
        LocalDateTime end = bookingRequest.endDateTime();

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }

        Booking existing = bookingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Booking not found with id: " + id));

        bookingUpdateRequestMapper.accept(existing, bookingRequest);

        Service service = serviceRepository.findById(bookingRequest.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service with id " + bookingRequest.serviceId() + " not found"));
        existing.setService(service);

        existing.setStatus(BookingStatus.valueOf(bookingRequest.status()));

        return bookingRepository.save(existing);
    }


    @Transactional
    public void remove(Long id) {
        bookingRepository.deleteById(id);
    }

}
