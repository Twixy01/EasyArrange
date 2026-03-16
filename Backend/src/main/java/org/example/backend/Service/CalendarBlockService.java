package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.backend.Model.entity.CalendarBlock;
import org.example.backend.Repository.CalendarBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalendarBlockService {

    private final CalendarBlockRepository calendarBlockRepository;

    @Autowired
    public CalendarBlockService(CalendarBlockRepository calendarBlockRepository) {
        this.calendarBlockRepository = calendarBlockRepository;
    }

    public CalendarBlock findCalendarBlockById(Long id) {
        return calendarBlockRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Calendar block not found with id: " + id));
    }

//    public CalendarBlock findByStartTime(LocalDateTime startTime) {
//        return calendarBlockRepository.findByStartTime(startTime).orElseThrow(() ->
//                new IllegalArgumentException("Calendar block not found with start time: " + startTime));
//    }

    public List<CalendarBlock> findAllByOrderByStartDatetimeAsc() {
        return calendarBlockRepository.findAllByOrderByStartDatetimeAsc();
    }

    public List<CalendarBlock> findCalendarBlocksByStaffId(Long staffId) {
        return calendarBlockRepository.findAllByStaffId(staffId);
    }

    public List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) {
        return calendarBlockRepository.findAllBetween(start, end);
    }

    public List<CalendarBlock> findCalendarBlocksByStaffBetween(Long staffId, LocalDateTime start, LocalDateTime end) {
        return calendarBlockRepository.findAllByStaffBetween(staffId, start, end);
    }

    @Transactional
    public CalendarBlock create(@Valid CalendarBlock calendarBlock) {
        if (calendarBlock.getStartDatetime() == null || calendarBlock.getEndDatetime() == null) {
            throw new IllegalArgumentException("Start and end datetime cannot be null");
        }

        LocalDateTime start = calendarBlock.getStartDatetime();
        LocalDateTime end = calendarBlock.getEndDatetime();

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }

        return calendarBlockRepository.save(calendarBlock);
    }

    @Transactional
    public CalendarBlock update(@Valid CalendarBlock calendarBlock) {
        if (calendarBlock.getStartDatetime() == null || calendarBlock.getEndDatetime() == null) {
            throw new IllegalArgumentException("Start and end datetime cannot be null");
        }

        LocalDateTime start = calendarBlock.getStartDatetime();
        LocalDateTime end = calendarBlock.getEndDatetime();

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }
        return calendarBlockRepository.save(calendarBlock);
    }

    @Transactional
    public void remove(Long id) {
        calendarBlockRepository.deleteById(id);
    }

}
