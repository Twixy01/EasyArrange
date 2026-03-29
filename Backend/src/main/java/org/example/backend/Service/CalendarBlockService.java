package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.CalendarBlock.CalendarBlockRequest;
import org.example.backend.DTO.CalendarBlock.CalendarBlockRequestMapper;
import org.example.backend.DTO.CalendarBlock.CalendarBlockResponse;
import org.example.backend.DTO.CalendarBlock.CalendarBlockResponseMapper;
import org.example.backend.Model.entity.CalendarBlock;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Repository.CalendarBlockRepository;
import org.example.backend.Repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarBlockService {

    private final CalendarBlockRepository calendarBlockRepository;
    private final StaffRepository staffRepository;
    private final CalendarBlockResponseMapper calendarBlockResponseMapper;
    private final CalendarBlockRequestMapper calendarBlockRequestMapper;

    @Autowired
    public CalendarBlockService(
            CalendarBlockRepository calendarBlockRepository,
            StaffRepository staffRepository,
            CalendarBlockResponseMapper calendarBlockResponseMapper,
            CalendarBlockRequestMapper calendarBlockRequestMapper) {
        this.calendarBlockRepository = calendarBlockRepository;
        this.staffRepository = staffRepository;
        this.calendarBlockResponseMapper = calendarBlockResponseMapper;
        this.calendarBlockRequestMapper = calendarBlockRequestMapper;
    }

    public CalendarBlockResponse findCalendarBlockById(Long calendarBlockId) {
        return calendarBlockRepository.findById(calendarBlockId)
                .map(calendarBlockResponseMapper)
                .orElseThrow(() ->
                new IllegalArgumentException("Calendar block not found with id: " + calendarBlockId));
    }

    public List<CalendarBlockResponse> findAllByOrderByStartDatetimeAsc() {
        return calendarBlockRepository.findAllByOrderByStartDatetimeAsc().stream()
                .map(calendarBlockResponseMapper)
                .collect(Collectors.toList());
    }

    public List<CalendarBlockResponse> findCalendarBlocksByStaffId(Long staffId) {
        return calendarBlockRepository.findAllByStaffId(staffId).stream()
                .map(calendarBlockResponseMapper)
                .collect(Collectors.toList());
    }

    public List<CalendarBlockResponse> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) {
        return calendarBlockRepository.findAllBetween(start, end).stream()
                .map(calendarBlockResponseMapper)
                .collect(Collectors.toList());
    }

    public List<CalendarBlockResponse> findCalendarBlocksByStaffBetween(Long staffId, LocalDateTime start, LocalDateTime end) {
        return calendarBlockRepository.findAllByStaffBetween(staffId, start, end).stream()
                .map(calendarBlockResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarBlockResponse create(CalendarBlockRequest calendarBlockDto) {
        Staff staff = staffRepository.findById(calendarBlockDto.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + calendarBlockDto.staffId()));

        String roleName = staff.getUser().getRole().getName();
        if (!roleName.equals("STAFF")) {
            throw new IllegalArgumentException("User with id: " + calendarBlockDto.staffId() + " does not have STAFF role");
        }

        CalendarBlock calendarBlock = new CalendarBlock();
        calendarBlockRequestMapper.accept(calendarBlockDto, calendarBlock);
        calendarBlock.setStaff(staff);

        calendarBlockRepository.save(calendarBlock);
        return calendarBlockResponseMapper.apply(calendarBlock);
    }

    @Transactional
    public CalendarBlockResponse update(Long calendarBlockId, CalendarBlockRequest calendarBlockDto) {
        CalendarBlock existingCalendarBlock = calendarBlockRepository.findById(calendarBlockId)
                .orElseThrow(() -> new IllegalArgumentException("Calendar block not found with id: " + calendarBlockId));

        Staff staff = staffRepository.findById(calendarBlockDto.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + calendarBlockDto.staffId()));

        calendarBlockRequestMapper.accept(calendarBlockDto, existingCalendarBlock);
        existingCalendarBlock.setStaff(staff);

        calendarBlockRepository.save(existingCalendarBlock);
        return calendarBlockResponseMapper.apply(existingCalendarBlock);
    }

    @Transactional
    public void remove(Long id) {
        calendarBlockRepository.deleteById(id);
    }

}
