package org.example.backend.Model.entity;

public enum BookingStatus {
    BOOKED,
    CANCELLED,
    COMPLETED,
    NO_SHOW;

    public boolean isActive() {
        return this == BOOKED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isNoShow() {
        return this == NO_SHOW;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED || this == NO_SHOW;
    }
}