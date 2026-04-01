package org.example.backend.Repository;

import org.example.backend.Model.entity.CalendarBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarBlockRepository extends JpaRepository<CalendarBlock, Long> {

    List<CalendarBlock> findAllByOrderByStartDateTimeAsc();

    List<CalendarBlock> findAllByStaffId(Long staffId);

    @Query("FROM CalendarBlock cb WHERE cb.startDateTime >= :start AND cb.endDateTime <= :end")
    List<CalendarBlock> findAllBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM CalendarBlock cb WHERE cb.staff.id = :staffId AND cb.startDateTime >= :start AND cb.endDateTime <= :end")
    List<CalendarBlock> findAllByStaffBetween(@Param("staffId") Long staffId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


}
