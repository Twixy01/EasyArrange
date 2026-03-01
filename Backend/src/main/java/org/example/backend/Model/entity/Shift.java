package org.example.backend.Model.entity;

import jakarta.persistence.*;

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

    @Lob
    @Column(name = "day", nullable = false)
    private String day;

    @Column(name = "start_shift", nullable = false)
    private LocalTime startShift;

    @Column(name = "end_shift", nullable = false)
    private LocalTime endShift;

    @ManyToMany(mappedBy = "shifts")
    private Set<Staff> staff = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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

    public Set<Staff> getStaff() {
        return staff;
    }

    public void setStaff(Set<Staff> staff) {
        this.staff = staff;
    }

}