package org.example.backend.Service;
import org.example.backend.DTO.CalendarBlock.CalendarBlockRequest;
import org.example.backend.DTO.CalendarBlock.CalendarBlockRequestMapper;
import org.example.backend.DTO.CalendarBlock.CalendarBlockResponse;
import org.example.backend.DTO.CalendarBlock.CalendarBlockResponseMapper;
import org.example.backend.Model.entity.CalendarBlock;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.CalendarBlockRepository;
import org.example.backend.Repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarBlockServiceTest {
    private static final Long STAFF_ID = 1L;
    @Mock private CalendarBlockRepository calendarBlockRepository;
    @Mock private StaffRepository staffRepository;
    @Mock private CalendarBlockResponseMapper calendarBlockResponseMapper;
    @Mock private CalendarBlockRequestMapper calendarBlockRequestMapper;

    private CalendarBlockService calendarBlockService;

    @BeforeEach
    void setUp() {
        calendarBlockService = new CalendarBlockService(
                calendarBlockRepository,
                staffRepository,
                calendarBlockResponseMapper,
                calendarBlockRequestMapper
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
    private CalendarBlock block(Long id, String title, LocalDateTime start, LocalDateTime end, Staff staff) {
        CalendarBlock block = new CalendarBlock();
        block.setCalendarBlockId(id);
        block.setTitle(title);
        block.setStartDateTime(start);
        block.setEndDateTime(end);
        block.setStaff(staff);
        return block;
    }
    @Test
    void create_success_savesBlockWithValidStaff() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(2);
        Staff staff = staff("STAFF");
        CalendarBlockRequest request = new CalendarBlockRequest("Meeting", start, end, STAFF_ID);
        CalendarBlockResponse expected = new CalendarBlockResponse(1L, "Meeting", start, end, null);
        when(calendarBlockRepository.existsCalendarBlockByStaffIdStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(false);
        when(calendarBlockRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(false);
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff));
        when(calendarBlockRepository.save(any(CalendarBlock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(calendarBlockResponseMapper.apply(any(CalendarBlock.class))).thenReturn(expected);

        doAnswer(invocation -> {
            CalendarBlockRequest req = invocation.getArgument(0);
            CalendarBlock block = invocation.getArgument(1);
            block.setTitle(req.title());
            block.setStartDateTime(req.startDateTime());
            block.setEndDateTime(req.endDateTime());
            return null;
        }).when(calendarBlockRequestMapper).accept(any(CalendarBlockRequest.class), any(CalendarBlock.class));

        CalendarBlockResponse actual = calendarBlockService.create(request);
        assertThat(actual).isSameAs(expected);

        ArgumentCaptor<CalendarBlock> captor = ArgumentCaptor.forClass(CalendarBlock.class);
        verify(calendarBlockRepository).save(captor.capture());
        CalendarBlock saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("Meeting");
        assertThat(saved.getStartDateTime()).isEqualTo(start);
        assertThat(saved.getEndDateTime()).isEqualTo(end);
        assertThat(saved.getStaff()).isEqualTo(staff);
    }
    @Test
    void create_rejectsDuplicateBlock() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(2);
        when(calendarBlockRepository.existsCalendarBlockByStaffIdStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(true);
        assertThatThrownBy(() -> calendarBlockService.create(new CalendarBlockRequest("Meeting", start, end, STAFF_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Calendar block already exists");
    }
    @Test
    void create_rejectsInvalidDateRange() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.minusHours(1);
        when(calendarBlockRepository.existsCalendarBlockByStaffIdStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(false);
        assertThatThrownBy(() -> calendarBlockService.create(new CalendarBlockRequest("Meeting", start, end, STAFF_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start datetime must be before end datetime");
    }
    @Test
    void create_rejectsOverlappingBlock() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(2);
        when(calendarBlockRepository.existsCalendarBlockByStaffIdStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(false);
        when(calendarBlockRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(true);
        assertThatThrownBy(() -> calendarBlockService.create(new CalendarBlockRequest("Meeting", start, end, STAFF_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Calendar block overlaps with existing block");
    }
    @Test
    void create_rejectsNonStaffRole() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 0);
        LocalDateTime end = start.plusHours(2);
        Staff staff = staff("CUSTOMER");
        when(calendarBlockRepository.existsCalendarBlockByStaffIdStartDateTimeAndEndDateTime(STAFF_ID, start, end)).thenReturn(false);
        when(calendarBlockRepository.existsOverlapping(STAFF_ID, start, end)).thenReturn(false);
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff));
        assertThatThrownBy(() -> calendarBlockService.create(new CalendarBlockRequest("Meeting", start, end, STAFF_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not have STAFF or ADMIN role");
    }
    @Test
    void update_changesDateTimeAndStaff() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 5, 14, 0);
        LocalDateTime end = start.plusHours(1);
        CalendarBlock existing = block(1L, "Old", LocalDateTime.of(2026, 5, 4, 10, 0), LocalDateTime.of(2026, 5, 4, 12, 0), staff("STAFF"));
        CalendarBlockRequest request = new CalendarBlockRequest("Updated", start, end, STAFF_ID);
        CalendarBlockResponse expected = new CalendarBlockResponse(1L, "Updated", start, end, null);
        when(calendarBlockRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(existing.getStaff()));
        when(calendarBlockRepository.save(any(CalendarBlock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(calendarBlockResponseMapper.apply(any(CalendarBlock.class))).thenReturn(expected);

        doAnswer(invocation -> {
            CalendarBlockRequest req = invocation.getArgument(0);
            CalendarBlock block = invocation.getArgument(1);
            block.setTitle(req.title());
            block.setStartDateTime(req.startDateTime());
            block.setEndDateTime(req.endDateTime());
            return null;
        }).when(calendarBlockRequestMapper).accept(any(CalendarBlockRequest.class), any(CalendarBlock.class));

        CalendarBlockResponse actual = calendarBlockService.update(1L, request);
        assertThat(actual).isSameAs(expected);

        ArgumentCaptor<CalendarBlock> captor = ArgumentCaptor.forClass(CalendarBlock.class);
        verify(calendarBlockRepository).save(captor.capture());
        CalendarBlock saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("Updated");
        assertThat(saved.getStartDateTime()).isEqualTo(start);
        assertThat(saved.getEndDateTime()).isEqualTo(end);
    }
    @Test
    void remove_deletesBlock() {
        calendarBlockService.remove(1L);
        verify(calendarBlockRepository).deleteById(1L);
    }
}