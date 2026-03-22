package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
    private List<Booking> bookings = new ArrayList<>();

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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<StaffService> getStaff() {
        return staff;
    }

    public void setStaff(Set<StaffService> staff) {
        this.staff = staff;
    }

}