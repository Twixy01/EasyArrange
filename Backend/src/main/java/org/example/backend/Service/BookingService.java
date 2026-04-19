package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.Booking.*;
import org.example.backend.DTO.TimeSlot.AvailableSlotResponse;
import org.example.backend.Model.entity.*;
import org.example.backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingResponseMapper responseMapper;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final BookingCreateRequestMapper bookingCreateRequestMapper;
    private final BookingUpdateRequestMapper bookingUpdateRequestMapper;
    private final BookingResponseMapper bookingResponseMapper;
    private final StaffShiftRepository staffShiftRepository;
    private final CalendarBlockRepository calendarBlockRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          BookingResponseMapper responseMapper,
                          StaffRepository staffRepository,
                          UserRepository userRepository,
                          ServiceRepository serviceRepository, BookingCreateRequestMapper bookingCreateRequestMapper, BookingUpdateRequestMapper bookingUpdateRequestMapper, BookingResponseMapper bookingResponseMapper, StaffShiftRepository staffShiftRepository, CalendarBlockRepository calendarBlockRepository) {
        this.bookingRepository = bookingRepository;
        this.responseMapper = responseMapper;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.bookingCreateRequestMapper = bookingCreateRequestMapper;
        this.bookingUpdateRequestMapper = bookingUpdateRequestMapper;
        this.bookingResponseMapper = bookingResponseMapper;
        this.staffShiftRepository = staffShiftRepository;
        this.calendarBlockRepository = calendarBlockRepository;
    }

    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingResponseMapper)
                .collect(Collectors.toList());


    }

    public BookingResponse findBookingById(long id) {
        return bookingRepository
                .findById(id).map(bookingResponseMapper)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id " + id + " not found"));
    }

    public List<BookingResponse> findBookingsByStaffId(Long staffId) {
        return bookingRepository.findAllByStaffId(staffId).stream()
                .map(responseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BookingResponse> findBookingsByCustomerId(Long customerId) {
        List<BookingResponse> bookings = bookingRepository.findAllByCustomerId(customerId).stream()
                .map(booking -> {
                    BookingStatus status = booking.getStatus();
                    if (status == BookingStatus.BOOKED && booking.getEndDateTime().isBefore(LocalDateTime.now())) {
                        status = BookingStatus.COMPLETED;
                    }
                    BookingUpdateRequest request = new BookingUpdateRequest(
                            booking.getStartDateTime(),
                            booking.getEndDateTime(),
                            booking.getService().getId(),
                            status.name()
                    );
                    return update(booking.getId(), request);
                })
                .collect(Collectors.toList());

        return bookings;
    }

    public List<BookingResponse> findBookingsBetween(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findAllBetween(start, end).stream()
                .map(bookingResponseMapper)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> findBookingsByStatus(org.example.backend.Model.entity.BookingStatus status) {
        return bookingRepository.findAllBookingsByStatus(status).stream()
                .map(bookingResponseMapper)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> findBookingsByStaffAtDatetimeAsc(Long staffId, java.time.LocalDateTime datetime) {
        return bookingRepository.findAllByOrderByStartDateTimeAsc().stream()
                .map(bookingResponseMapper)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> findBookingsByStaffAtDatetimeDesc(Long staffId, java.time.LocalDateTime datetime) {
        return bookingRepository.findAllByOrderByStartDateTimeDesc().stream()
                .map(bookingResponseMapper)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> findOverlappingBookings(Long staffId, LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }

        return bookingRepository.findAllOverlaps(staffId, start, end).stream()
                .map(bookingResponseMapper)
                .collect(Collectors.toList());
    }

    public List<AvailableSlotResponse> getAvailableSlots(Long staffId, LocalDate selectedDate, Long serviceId) {
        List<Shift> shifts = staffShiftRepository.findAllShiftsByStaffId(staffId);

        Shift shiftBySelectedDate = shifts.stream()
                .filter(shift -> shift.getDay().name().equals(selectedDate.getDayOfWeek().name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No shift found for staff with id " + staffId + " on date " + selectedDate));

        LocalDateTime startOfDay = LocalDateTime.of(selectedDate, shiftBySelectedDate.getStartShift());
        LocalDateTime endOfDay = LocalDateTime.of(selectedDate, shiftBySelectedDate.getEndShift());

        int serviceDuration = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service with id " + serviceId + " not found"))
                .getDuration();

        List<AvailableSlotResponse> slots = new ArrayList<>();

        LocalDateTime current = startOfDay;

        LocalDateTime now = LocalDateTime.now().plusHours(3); //You can book a slot at least 3 hours in advance
        while (current.plusMinutes(serviceDuration).isBefore(endOfDay)
                || current.plusMinutes(serviceDuration).isEqual(endOfDay)) {

            if (current.isBefore(now)) {
                current = current.plusMinutes(15);
                continue; // Skip past time slots
            }
            LocalDateTime end = current.plusMinutes(serviceDuration);

            boolean overlaps = bookingRepository.existsOverlapping(
                    staffId,
                    current,
                    end
            );

            boolean blocked = calendarBlockRepository.existsOverlapping(
                    staffId,
                    current,
                    end
            );

            if (!overlaps && !blocked) {
                slots.add(new AvailableSlotResponse(
                        current,
                        end,
                        current.toLocalTime().toString()
                ));
            }

            current = current.plusMinutes(15);
        }

        return slots;
    }

    @Transactional
    public BookingResponse create(BookingCreateRequest bookingRequest) {

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

        //Booking status is set to BOOKED by default in the Booking entity, so we don't need to set it here

        bookingRepository.save(booking);

        return responseMapper.apply(booking);
    }

    @Transactional
    public BookingResponse update(Long id, BookingUpdateRequest bookingRequest) {

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

        bookingRepository.save(existing);
        return bookingResponseMapper.apply(existing);
    }


    @Transactional
    public void cancel(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Booking not found with id: " + id));

        LocalDateTime now = LocalDateTime.now();
        // Block cancellation if booking.startDateTime is not after now + 24 hours
        if (!booking.getStartDateTime().isAfter(now.plusHours(24))) {
            throw new IllegalArgumentException("Cannot cancel booking less than or equal to 24 hours before the start time");
        }

        //mark it as CANCELLED and persist the status
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void remove(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Booking not found with id: " + id));

        bookingRepository.deleteById(booking.getId());
    }

}
