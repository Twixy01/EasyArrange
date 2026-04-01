package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_block", schema = "easyarrange", uniqueConstraints = {@UniqueConstraint(name = "staff_start_end",
        columnNames = {
                "staff_id",
                "start_datetime",
                "end_datetime"})})
public class CalendarBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_block_id", nullable = false)
    private Long id;

    @Column(name = "start_datetime", nullable = false)
    @NotNull(message = "Start datetime can't be null")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    @NotNull(message = "End datetime can't be null")
    private LocalDateTime endDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "staff_id", nullable = false)
    @NotNull(message = "Staff can't be null")
    private Staff staff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

}