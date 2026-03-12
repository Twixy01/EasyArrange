package org.example.backend.Model.entity;

public enum ShiftDay {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int dayNumber;

    ShiftDay(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public boolean isWeekday() {
        return this != SATURDAY && this != SUNDAY;
    }

    public boolean isWeekend() {
        return this == SATURDAY || this == SUNDAY;
    }
}
