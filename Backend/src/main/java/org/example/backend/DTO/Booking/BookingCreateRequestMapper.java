package org.example.backend.DTO.Booking;

import org.example.backend.Model.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookingCreateRequestMapper implements Function<BookingCreateRequest, Booking> {

    @Override
    public Booking apply(BookingCreateRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setStartDatetime(bookingRequest.startDateTime());
        booking.setEndDatetime(bookingRequest.endDateTime());
        return booking;
    }
}
