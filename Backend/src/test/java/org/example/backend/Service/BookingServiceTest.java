package org.example.backend.Service;
import org.example.backend.DTO.Booking.BookingCreateRequest;
import org.example.backend.DTO.Booking.BookingCreateRequestMapper;
import org.example.backend.DTO.Booking.BookingResponse;
import org.example.backend.DTO.Booking.BookingResponseMapper;
import org.example.backend.DTO.Booking.BookingUpdateRequest;
import org.example.backend.DTO.Booking.BookingUpdateRequestMapper;
import org.example.backend.DTO.TimeSlot.AvailableSlotResponse;
import org.example.backend.Model.entity.Booking;
import org.example.backend.Model.entity.BookingStatus;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Model.entity.ShiftDay;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.BookingRepository;
import org.example.backend.Repository.CalendarBlockRepository;
import org.example.backend.Repository.ServiceRepository;
import org.example.backend.Repository.StaffRepository;
import org.example.backend.Repository.StaffShiftRepository;
import org.example.backend.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private static final Long STAFF_ID = 1L;
    private static final Long USER_ID = 2L;
    private static final Long SERVICE_ID = 3L;
    @Mock private BookingRepository bookingRepository;
    @Mock private BookingResponseMapper responseMapper;
    @Mock private StaffRepository staffRepository;
    @Mock private UserRepository userRepository;
    @Mock private ServiceRepository serviceRepository;
    @Mock private BookingCreateRequestMapper bookingCreateRequestMapper;
    @Mock private BookingUpdateRequestMapper bookingUpdateRequestMapper;
    @Mock private BookingResponseMapper bookingResponseMapper;
    @Mock private StaffShiftRepository staffShiftRepository;
    @Mock private CalendarBlockRepository calendarBlockRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, responseMapper, staffRepository, userRepository, serviceRepository, bookingCreateRequestMapper, bookingUpdateRequestMapper, bookingResponseMapper, staffShiftRepository, calendarBlockRepository);
    }
    private Role role(String name) {
        Role role = new Role();
        role.setRoleId(10L);
        role.setName(name);
        return role;
    }
    private User user(Long id, String roleName) {
        User user = new User();
        user.setId(id);
        user.setName("User " + id);
        user.setEmail("user" + id + "@example.com");
        user.setPhoneNumber("+36301234567");
        user.setPassword("secret");
        user.setRole(role(roleName));
        return user;
    }
    private Staff staff(Long id, String roleName) {
        Staff staff = new Staff();
        staff.setId(id);
        staff.setUser(user(100L + id, roleName));
        return staff;
    }
    private Service service(Long id, int duration) {
        Service service = new Service();
        service.setId(id);
        service.setName("Service " + id);
        service.setDuration(duration);
        service.setPrice(5000);
        return service;
    }
    private Booking booking(Long id, Staff staff, User user, Service service, LocalDateTime start, LocalDateTime end, BookingStatus status) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setStaff(staff);
        booking.setUser(user);
        booking.setService(service);
        booking.setStartDateTime(start);
        booking.setEndDateTime(end);
        booking.setStatus(status);
        return booking;
    }
    @Test
    void create_success_persistsAndMapsBooking() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(1);
        BookingCreateRequest request = new BookingCreateRequest(STAFF_ID, USER_ID, start, end, SERVICE_ID);
        Booking mappedBooking = booking(null, staff(STAFF_ID, "STAFF"), user(USER_ID, "CUSTOMER"), service(SERVICE_ID, 60), start, end, BookingStatus.BOOKED);
        BookingResponse expected = new BookingResponse(99L, null, null, start, end, null, "BOOKED");
        when(bookingRepository.existsByStaffIdAndStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(false);
        when(calendarBlockRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(false);
        when(bookingRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(false);
        when(bookingCreateRequestMapper.apply(request)).thenReturn(mappedBooking);
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(mappedBooking.getStaff()));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mappedBooking.getUser()));
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(mappedBooking.getService()));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(responseMapper.apply(any(Booking.class))).thenReturn(expected);
        BookingResponse actual = bookingService.create(request);
        assertThat(actual).isSameAs(expected);
        verify(bookingRepository).save(mappedBooking);
    }
    @Test
    void create_rejectsOverlappingBooking() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(1);
        when(bookingRepository.existsByStaffIdAndStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(false);
        when(calendarBlockRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(false);
        when(bookingRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(true);
        assertThatThrownBy(() -> bookingService.create(new BookingCreateRequest(STAFF_ID, USER_ID, start, end, SERVICE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("overlaps with a calendar block or booking");
    }
    @Test
    void getAvailableSlots_returnsOnlyFreeSlots() {
        LocalDate selectedDate = LocalDate.of(2026, 5, 4);
        Shift shift = new Shift();
        shift.setId(1L);
        shift.setDay(ShiftDay.MONDAY);
        shift.setStartShift(LocalTime.of(9, 0));
        shift.setEndShift(LocalTime.of(11, 0));
        when(staffShiftRepository.findAllShiftsByStaffId(STAFF_ID)).thenReturn(List.of(shift));
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service(SERVICE_ID, 60)));
        when(bookingRepository.existsOverlapping(eq(STAFF_ID), any(), any())).thenAnswer(invocation -> invocation.getArgument(1, LocalDateTime.class).equals(selectedDate.atTime(9, 15)));
        when(calendarBlockRepository.existsOverlapping(eq(STAFF_ID), any(), any())).thenAnswer(invocation -> invocation.getArgument(1, LocalDateTime.class).equals(selectedDate.atTime(9, 45)));
        List<AvailableSlotResponse> actual = bookingService.getAvailableSlots(STAFF_ID, selectedDate, SERVICE_ID);
        assertThat(actual).extracting(AvailableSlotResponse::startDateTime)
                .containsExactly(selectedDate.atTime(9, 0), selectedDate.atTime(9, 30), selectedDate.atTime(10, 0));
    }
    @Test
    void update_reactivatingCancelledBooking_checksConflicts() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(1);
        Booking existing = booking(11L, staff(STAFF_ID, "STAFF"), user(USER_ID, "CUSTOMER"), service(SERVICE_ID, 60), start, end, BookingStatus.CANCELLED);
        when(bookingRepository.findById(11L)).thenReturn(Optional.of(existing));
        when(calendarBlockRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(true);
        assertThatThrownBy(() -> bookingService.update(11L, new BookingUpdateRequest(start, end, SERVICE_ID, "BOOKED"), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("overlaps with a calendar block or booking");
        verify(bookingRepository, never()).save(any());
    }
    @Test
    void cancel_rejectsBookingWithin24Hours() {
        LocalDateTime start = LocalDateTime.now().plusHours(12);
        Booking existing = booking(11L, staff(STAFF_ID, "STAFF"), user(USER_ID, "CUSTOMER"), service(SERVICE_ID, 60), start, start.plusHours(1), BookingStatus.BOOKED);
        when(bookingRepository.findById(11L)).thenReturn(Optional.of(existing));
        assertThatThrownBy(() -> bookingService.cancel(11L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot cancel booking less than or equal to 24 hours before the start time");
    }
}