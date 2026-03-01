package org.example.backend.Model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StaffShiftId implements Serializable {
    private static final long serialVersionUID = -7404898564777180939L;
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "shift_id", nullable = false)
    private Long shiftId;

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffShiftId entity = (StaffShiftId) o;
        return Objects.equals(this.staffId, entity.staffId) &&
                Objects.equals(this.shiftId, entity.shiftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId, shiftId);
    }
}