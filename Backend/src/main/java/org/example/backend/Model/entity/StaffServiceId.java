package org.example.backend.Model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StaffServiceId implements Serializable {
    private static final long serialVersionUID = 7724587181440375533L;
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffServiceId entity = (StaffServiceId) o;
        return Objects.equals(this.staffId, entity.staffId) &&
                Objects.equals(this.serviceId, entity.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId, serviceId);
    }
}