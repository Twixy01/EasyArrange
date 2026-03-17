package org.example.backend.DTO.Booking;

import org.example.backend.Model.entity.Booking;

import java.util.function.Function;

public class BookingUpdateRequestMapper implements Function<Booking, BookingUpdateRequest> {
    @Override
    public BookingUpdateRequest apply(Booking booking) {
        return new BookingUpdateRequest(
                booking.getStartDatetime(),
                booking.getEndDatetime(),
                booking.getService().getId(),
                booking.getStatus().name()
        );
    }
}
