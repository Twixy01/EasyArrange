package org.example.backend.DTO.CalendarBlock;

import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.Model.entity.CalendarBlock;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CalendarBlockResponseMapper implements Function<CalendarBlock, CalendarBlockResponse> {
    private final StaffResponseMapper staffResponseMapper;

    public CalendarBlockResponseMapper(StaffResponseMapper staffResponseMapper) {
        this.staffResponseMapper = staffResponseMapper;
    }

    @Override
    public CalendarBlockResponse apply(CalendarBlock calendarBlock) {
        return new CalendarBlockResponse(
                calendarBlock.getId(),
                calendarBlock.getStartDatetime(),
                calendarBlock.getEndDatetime(),
                staffResponseMapper.apply(calendarBlock.getStaff())
        );
    }
}
