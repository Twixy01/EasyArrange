package org.example.backend.Model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "staff_shift", schema = "easyarrange")
public class StaffShift {
    @EmbeddedId
    private StaffShiftId id;

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
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

}