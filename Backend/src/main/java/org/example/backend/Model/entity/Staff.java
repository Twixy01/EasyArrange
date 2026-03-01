package org.example.backend.Model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "staff", schema = "easyarrange", uniqueConstraints = {@UniqueConstraint(name = "user_id",
        columnNames = {"user_id"})})
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "staff")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "staff")
    private Set<CalendarBlock> calendarBlocks = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "staff")
    private Set<Service> services = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "staff")
    private Set<Shift> shifts = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<CalendarBlock> getCalendarBlocks() {
        return calendarBlocks;
    }

    public void setCalendarBlocks(Set<CalendarBlock> calendarBlocks) {
        this.calendarBlocks = calendarBlocks;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }

}