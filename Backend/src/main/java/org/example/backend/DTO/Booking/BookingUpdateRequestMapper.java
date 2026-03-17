package org.example.backend.DTO.Booking;

import org.example.backend.Model.entity.Booking;
import org.example.backend.Model.entity.BookingStatus;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookingUpdateRequestMapper implements Function<BookingUpdateRequest, Booking> {
    @Override
    public Booking apply(BookingUpdateRequest request) {
        Booking booking = new Booking();
        booking.setStartDatetime(request.startDateTime());
        booking.setEndDatetime(request.endDateTime());

        if (request.status() != null) {
            try {
                booking.setStatus(BookingStatus.valueOf(request.status()));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid booking status: " + request.status(), ex);
            }
        }

        return booking;
    }
}
