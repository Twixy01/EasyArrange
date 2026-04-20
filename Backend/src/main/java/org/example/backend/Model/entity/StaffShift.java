package org.example.backend.Model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "staff_shift", schema = "easyarrange")
public class StaffShift {
    @EmbeddedId
    private StaffShiftId id = new StaffShiftId();

    @MapsId("staffId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @MapsId("shiftId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    public StaffShiftId getId() {
        return id;
    }

    public void setId(StaffShiftId id) {
        this.id = (id != null) ? id : new StaffShiftId();
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
        if (id == null) {
            id = new StaffShiftId();
        }
        id.setStaffId(staff != null ? staff.getId() : null);
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
        if (id == null){
            id = new StaffShiftId();
        }
        id.setShiftId(shift != null ? shift.getId() : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StaffShift that = (StaffShift) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}