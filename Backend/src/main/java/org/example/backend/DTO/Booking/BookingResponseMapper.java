package org.example.backend.DTO.Booking;

import org.example.backend.Model.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookingResponseMapper implements Function<Booking, BookingResponse> {
    @Override
    public BookingResponse apply(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getStaff().getId(),
                booking.getCustomer().getId(),
                booking.getStartDatetime(),
                booking.getEndDatetime(),
                booking.getService().getId(),
                booking.getStatus().name()
        );
    }
}
