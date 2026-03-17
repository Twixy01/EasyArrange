package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking", schema = "easyarrange", uniqueConstraints = {@UniqueConstraint(name = "uq_booking_staff_start_end",
        columnNames = {
                "staff_id",
                "start_datetime",
                "end_datetime"})})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "staff_id", nullable = false)
    @NotNull(message = "Staff can't be null")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer can't be null")
    private User customer;

    @Column(name = "start_datetime", nullable = false)
    @NotNull(message = "Start datetime can't be null")
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    @NotNull(message = "End datetime can't be null")
    private LocalDateTime endDatetime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull(message = "Service can't be null")
    private Service service;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "Status can't be null")
    private BookingStatus status = BookingStatus.BOOKED;

    public boolean isCancelled() {
        return status.isCancelled();
    }

    public boolean isCompleted() {
        return status.isCompleted();
    }

    public boolean isNoShow() {
        return status.isNoShow();
    }

    // Helper: considered active when BOOKED
    public boolean isActive() {
        return status.isActive();
    }

    public boolean isFinal(){
        return status.isFinal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}