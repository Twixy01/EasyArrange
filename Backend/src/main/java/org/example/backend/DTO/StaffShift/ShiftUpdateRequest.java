package org.example.backend.DTO.StaffShift;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalTime;

public record ShiftUpdateRequest(
        @NotNull(message = "Staff ID cannot be null")
        @Positive(message = "Staff ID must be a positive number")
        Long staffId,

        @NotBlank(message = "Day cannot be blank")
        String day,

        @NotNull(message = "Shift time can't be null")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startShift,

        @NotNull(message = "Shift time can't be null")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endShift
) {
}
