package org.example.backend.Model.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "service", schema = "easyarrange", uniqueConstraints = {@UniqueConstraint(name = "name",
        columnNames = {"name"})})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "duration")
    private Integer duration;

    @OneToMany(mappedBy = "service")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "service")
    private Set<StaffService> staff = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<StaffService> getStaff() {
        return staff;
    }

    public void setStaff(Set<StaffService> staff) {
        this.staff = staff;
    }

}