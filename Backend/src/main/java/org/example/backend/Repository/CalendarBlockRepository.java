package org.example.backend.Repository;

import org.example.backend.Model.entity.CalendarBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarBlockRepository extends JpaRepository<CalendarBlock, Long> {

    Optional<CalendarBlock> findCalendarBlockById(Long id);
    Optional<CalendarBlock> findByStartTime(LocalDateTime startTime);
    List<CalendarBlock> findAllByOrderByStartTimeAsc();
    List<CalendarBlock> findCalendarBlocksByStaffId(long staffId);
    List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end);
    List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end);


}
