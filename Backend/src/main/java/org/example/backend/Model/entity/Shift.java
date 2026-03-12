package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "shift", schema = "easyarrange", uniqueConstraints = {@UniqueConstraint(name = "uk_shift_unique",
        columnNames = {
                "day",
                "start_shift",
                "end_shift"})})
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id", nullable = false)
    private Long id;

    @Column(name = "day", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Day must not be null")
    private ShiftDay day;

    @Column(name = "start_shift", nullable = false)
    @NotNull(message = "Shift start time must not be null")
    private LocalTime startShift;

    @Column(name = "end_shift", nullable = false)
    @NotNull(message = "Shift end time must not be null")
    private LocalTime endShift;

    @OneToMany(mappedBy = "shift")
    private Set<StaffShift> staff = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShiftDay getDay() {
        return day;
    }

    public void setDay(ShiftDay day) {
        this.day = day;
    }

    public LocalTime getStartShift() {
        return startShift;
    }

    public void setStartShift(LocalTime startShift) {
        this.startShift = startShift;
    }

    public LocalTime getEndShift() {
        return endShift;
    }

    public void setEndShift(LocalTime endShift) {
        this.endShift = endShift;
    }

    public Set<StaffShift> getStaff() {
        return staff;
    }

    public void setStaff(Set<StaffShift> staff) {
        this.staff = staff;
    }

}