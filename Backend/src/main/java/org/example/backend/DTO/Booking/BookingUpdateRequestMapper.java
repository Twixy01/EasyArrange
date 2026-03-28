package org.example.backend.DTO.Booking;

import org.example.backend.Model.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class BookingUpdateRequestMapper implements BiConsumer<Booking, BookingUpdateRequest> {
    @Override
    public void accept(Booking booking, BookingUpdateRequest bookingUpdateRequest) {
        booking.setStartDatetime(bookingUpdateRequest.startDateTime());
        booking.setEndDatetime(bookingUpdateRequest.endDateTime());

    }
}
