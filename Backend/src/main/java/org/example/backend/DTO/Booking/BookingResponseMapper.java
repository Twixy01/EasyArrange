package org.example.backend.DTO.Booking;

import org.example.backend.DTO.Service.ServiceResponseMapper;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.DTO.User.UserResponseMapper;
import org.example.backend.Model.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookingResponseMapper implements Function<Booking, BookingResponse> {
    private final StaffResponseMapper staffResponseMapper;
    private final UserResponseMapper userResponseMapper;
    private final ServiceResponseMapper serviceResponseMapper;

    public BookingResponseMapper(StaffResponseMapper staffResponseMapper, UserResponseMapper userResponseMapper, ServiceResponseMapper serviceResponseMapper) {
        this.staffResponseMapper = staffResponseMapper;
        this.userResponseMapper = userResponseMapper;
        this.serviceResponseMapper = serviceResponseMapper;
    }

    @Override
    public BookingResponse apply(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                staffResponseMapper.apply(booking.getStaff()),
                userResponseMapper.apply(booking.getUser()),
                booking.getStartDateTime(),
                booking.getEndDateTime(),
                serviceResponseMapper.apply(booking.getService()),
                booking.getStatus().name()
        );
    }
}
