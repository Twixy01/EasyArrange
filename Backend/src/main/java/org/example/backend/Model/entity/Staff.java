package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "staff")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "staff")
    private List<CalendarBlock> calendarBlocks = new ArrayList<>();

    @OneToMany(mappedBy = "staff")
    private Set<StaffServiceJunction> services = new LinkedHashSet<>();

    @OneToMany(mappedBy = "staff")
    private Set<StaffShift> shifts = new LinkedHashSet<>();

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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<CalendarBlock> getCalendarBlocks() {
        return calendarBlocks;
    }

    public void setCalendarBlocks(List<CalendarBlock> calendarBlocks) {
        this.calendarBlocks = calendarBlocks;
    }

    public Set<StaffServiceJunction> getServices() {
        return services;
    }

    public void setServices(Set<StaffServiceJunction> services) {
        this.services = services;
    }

    public Set<StaffShift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<StaffShift> shifts) {
        this.shifts = shifts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}