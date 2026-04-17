package org.example.backend.DTO.CalendarBlock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CalendarBlockRequest(
        @NotBlank(message = "Title can't be blank")
        String title,
        @NotNull(message = "Start datetime can't be null")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startDateTime,
        @NotNull(message = "End datetime can't be null")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endDateTime,
        @NotNull(message = "Staff ID can't be null")
        @Positive(message = "Staff ID must be positive")
        Long staffId
) {
    @JsonIgnore
    @AssertTrue(message = "Start datetime must be before end datetime")
    public boolean isDateRangeValid() {
        return startDateTime == null || endDateTime == null || startDateTime.isBefore(endDateTime);
    }
}
