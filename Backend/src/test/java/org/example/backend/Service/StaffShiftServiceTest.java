package org.example.backend.Service;
import org.example.backend.DTO.Shift.ShiftResponseMapper;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.DTO.StaffShift.ShiftUpdateRequest;
import org.example.backend.DTO.StaffShift.StaffShiftResponse;
import org.example.backend.DTO.StaffShift.StaffShiftResponseMapper;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Model.entity.ShiftDay;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffShift;
import org.example.backend.Model.entity.StaffShiftId;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.ShiftRepository;
import org.example.backend.Repository.StaffRepository;
import org.example.backend.Repository.StaffShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StaffShiftServiceTest {
    private static final Long STAFF_ID = 1L;
    @Mock private StaffShiftRepository staffShiftRepository;
    @Mock private StaffShiftResponseMapper staffShiftResponseMapper;
    @Mock private StaffResponseMapper staffResponseMapper;
    @Mock private StaffRepository staffRepository;
    @Mock private ShiftRepository shiftRepository;
    @Mock private ShiftResponseMapper shiftResponseMapper;

    private StaffShiftService staffShiftService;

    @BeforeEach
    void setUp() {
        staffShiftService = new StaffShiftService(
                staffShiftRepository,
                staffShiftResponseMapper,
                staffResponseMapper,
                staffRepository,
                shiftRepository,
                shiftResponseMapper
        );
    }
    private Role role(String name) {
        Role role = new Role();
        role.setRoleId(10L);
        role.setName(name);
        return role;
    }
    private User user(String roleName) {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhoneNumber("+36301234567");
        user.setPassword("secret");
        user.setRole(role(roleName));
        return user;
    }
    private Staff staff(String roleName) {
        Staff staff = new Staff();
        staff.setId(STAFF_ID);
        staff.setUser(user(roleName));
        return staff;
    }
    private Shift shift(ShiftDay day, LocalTime start, LocalTime end) {
        Shift shift = new Shift();
        shift.setId(1L);
        shift.setDay(day);
        shift.setStartShift(start);
        shift.setEndShift(end);
        return shift;
    }
    private StaffShift staffShift(Staff staff, Shift shift) {
        StaffShift ss = new StaffShift();
        ss.setStaff(staff);
        ss.setShift(shift);
        StaffShiftId id = new StaffShiftId();
        id.setStaffId(staff.getId());
        id.setShiftId(shift.getId());
        ss.setId(id);
        return ss;
    }
    @Test
    void updateShiftForStaffDay_createsNewShiftWhenDoesNotExist() {
        Staff staff = staff("STAFF");
        Shift shift = shift(ShiftDay.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "MONDAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
        StaffShift staffShift = staffShift(staff, shift);
        StaffShiftResponse expected = new StaffShiftResponse(null, null);
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff));
        when(shiftRepository.findByDayAndStartShiftAndEndShift(ShiftDay.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .thenReturn(Optional.empty());
        when(shiftRepository.save(any(Shift.class))).thenAnswer(invocation -> {
            Shift s = invocation.getArgument(0);
            s.setId(1L);
            return s;
        });
        when(staffShiftRepository.findStaffShiftByStaffAndShiftDay(STAFF_ID, ShiftDay.MONDAY)).thenReturn(Optional.empty());
        when(staffShiftRepository.save(any(StaffShift.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(staffShiftResponseMapper.apply(any(StaffShift.class))).thenReturn(expected);
        StaffShiftResponse actual = staffShiftService.updateShiftForStaffDay(request);
        assertThat(actual).isSameAs(expected);
        verify(shiftRepository).save(any(Shift.class));
        verify(staffShiftRepository).save(any(StaffShift.class));
    }
    @Test
    void updateShiftForStaffDay_replacesExistingShiftForDay() {
        Staff staff = staff("STAFF");
        Shift oldShift = shift(ShiftDay.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        oldShift.setId(1L);
        Shift newShift = shift(ShiftDay.MONDAY, LocalTime.of(10, 0), LocalTime.of(18, 0));
        newShift.setId(2L);
        StaffShift oldStaffShift = staffShift(staff, oldShift);
        StaffShift newStaffShift = staffShift(staff, newShift);
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "MONDAY", LocalTime.of(10, 0), LocalTime.of(18, 0));
        StaffShiftResponse expected = new StaffShiftResponse(null, null);
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff));
        when(shiftRepository.findByDayAndStartShiftAndEndShift(ShiftDay.MONDAY, LocalTime.of(10, 0), LocalTime.of(18, 0)))
                .thenReturn(Optional.of(newShift));
        when(staffShiftRepository.findStaffShiftByStaffAndShiftDay(STAFF_ID, ShiftDay.MONDAY)).thenReturn(Optional.of(oldStaffShift));
        when(staffShiftRepository.save(any(StaffShift.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(staffShiftResponseMapper.apply(any(StaffShift.class))).thenReturn(expected);
        StaffShiftResponse actual = staffShiftService.updateShiftForStaffDay(request);
        ArgumentCaptor<StaffShift> captor = ArgumentCaptor.forClass(StaffShift.class);
        verify(staffShiftRepository).delete(oldStaffShift);
        verify(staffShiftRepository).save(captor.capture());
        assertThat(captor.getValue().getShift().getId()).isEqualTo(newShift.getId());
        assertThat(actual).isSameAs(expected);
    }
    @Test
    void updateShiftForStaffDay_returnsIdempotentWhenSameShift() {
        Staff staff = staff("STAFF");
        Shift shift = shift(ShiftDay.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        shift.setId(1L);
        StaffShift existing = staffShift(staff, shift);
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "MONDAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
        StaffShiftResponse expected = new StaffShiftResponse(null, null);
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff));
        when(shiftRepository.findByDayAndStartShiftAndEndShift(ShiftDay.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .thenReturn(Optional.of(shift));
        when(staffShiftRepository.findStaffShiftByStaffAndShiftDay(STAFF_ID, ShiftDay.MONDAY)).thenReturn(Optional.of(existing));
        when(staffShiftResponseMapper.apply(existing)).thenReturn(expected);
        StaffShiftResponse actual = staffShiftService.updateShiftForStaffDay(request);
        assertThat(actual).isSameAs(expected);
        verify(staffShiftRepository, never()).delete(any());
        verify(staffShiftRepository, never()).save(any());
    }
    @Test
    void updateShiftForStaffDay_rejectsInvalidShiftTimes() {
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "MONDAY", LocalTime.of(17, 0), LocalTime.of(9, 0));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff("STAFF")));
        assertThatThrownBy(() -> staffShiftService.updateShiftForStaffDay(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Shift start time must be before end time");
    }
    @Test
    void updateShiftForStaffDay_rejectsShiftShorterThanOneHour() {
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "MONDAY", LocalTime.of(9, 0), LocalTime.of(9, 30));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff("STAFF")));
        assertThatThrownBy(() -> staffShiftService.updateShiftForStaffDay(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Shift must be at least 1 hour long");
    }
    @Test
    void updateShiftForStaffDay_rejectsNonStaffRole() {
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "MONDAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff("CUSTOMER")));
        assertThatThrownBy(() -> staffShiftService.updateShiftForStaffDay(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot have shift updates");
    }
    @Test
    void updateShiftForStaffDay_rejectsInvalidDay() {
        ShiftUpdateRequest request = new ShiftUpdateRequest(STAFF_ID, "INVALID_DAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff("STAFF")));
        assertThatThrownBy(() -> staffShiftService.updateShiftForStaffDay(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Day must be one of");
    }
}