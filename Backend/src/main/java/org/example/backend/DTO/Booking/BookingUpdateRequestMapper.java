package org.example.backend.DTO.Booking;

import org.example.backend.Model.entity.Booking;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BookingUpdateRequestMapper implements BiConsumer<Booking, BookingUpdateRequest> {
    @Override
    public void accept(Booking booking, BookingUpdateRequest bookingUpdateRequest) {
        booking.setStartDatetime(bookingUpdateRequest.startDateTime());
        booking.setEndDatetime(bookingUpdateRequest.endDateTime());

    }
//    @Override
//    public BookingUpdateRequest apply(Booking booking) {
//        return new BookingUpdateRequest(
//                booking.getStartDatetime(),
//                booking.getEndDatetime(),
//                booking.getService().getId(),
//                booking.getStatus().name()
//        );
//    }

}
