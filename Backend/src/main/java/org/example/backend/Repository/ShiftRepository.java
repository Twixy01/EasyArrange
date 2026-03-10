package org.example.backend.Repository;

import org.example.backend.Model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findAllShiftsByStartShift(LocalTime startShift);

    List<Shift> findAllShiftsByEndShift(LocalTime endShift);

    @Query("FROM Shift s WHERE s.startShift >= :startShift AND s.endShift <= :endShit")
    List<Shift> findAllShiftsBetweenShifts(@Param("startShift") LocalTime startShift, @Param("endShit") LocalTime endShift);
}
