package org.example.backend.Repository;

import org.example.backend.Model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {


    Optional<Shift> findShiftById(Long id);
    List<Shift> findAllShiftsByStaffId(long staffId);
    List<Shift> findAllShiftsByTime(LocalTime time);
    List<Shift> findShiftsBetweenTime(LocalTime startTime, LocalTime endTime);
    List<Shift> findShiftsByStaffIdBetweenTimes(long staff_id, LocalTime startTime, LocalTime endTime);

}
