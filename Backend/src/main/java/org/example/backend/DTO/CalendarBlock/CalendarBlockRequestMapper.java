package org.example.backend.DTO.CalendarBlock;

import org.example.backend.Model.entity.CalendarBlock;
import org.example.backend.Model.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CalendarBlockRequestMapper implements BiConsumer<CalendarBlockRequest, CalendarBlock> {
    @Override
    public void accept(CalendarBlockRequest calendarBlockRequest, CalendarBlock calendarBlock) {
        calendarBlock.setTitle(calendarBlockRequest.title());
        calendarBlock.setStartDateTime(calendarBlockRequest.startDateTime());
        calendarBlock.setEndDateTime(calendarBlockRequest.endDateTime());

        Staff staff = new Staff();
        staff.setId(calendarBlockRequest.staffId());
        calendarBlock.setStaff(staff);
    }
}
