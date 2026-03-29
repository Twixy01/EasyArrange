package org.example.backend.DTO.CalendarBlock;

import org.example.backend.Model.entity.CalendarBlock;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CalendarBlockResponseMapper implements Function<CalendarBlock, CalendarBlockResponse> {
    @Override
    public CalendarBlockResponse apply(CalendarBlock calendarBlock) {
        return new CalendarBlockResponse(
                calendarBlock.getId(),
                calendarBlock.getStartDatetime(),
                calendarBlock.getEndDatetime(),
                calendarBlock.getStaff().getId()
        );
    }
}
