package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "staff_service", schema = "easyarrange")
public class StaffServiceJunction {
    @EmbeddedId
    private StaffServiceId id = new StaffServiceId();

    @MapsId("staffId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "staff_id", nullable = false)
    @NotNull(message = "Staff cannot be null")
    private Staff staff;

    @MapsId("serviceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull(message = "Service cannot be null")
    private Service service;

    public StaffServiceId getId() {
        return id;
    }

    public void setId(StaffServiceId id) {
        this.id = (id != null) ? id : new StaffServiceId();
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
        if (id == null) {
            id = new StaffServiceId();
        }
        id.setStaffId(staff != null ? staff.getId() : null);
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
        if (id == null) {
            id = new StaffServiceId();
        }
        id.setServiceId(service != null ? service.getId() : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StaffServiceJunction that = (StaffServiceJunction) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Hibernate.getClass(this),id);
    }

}