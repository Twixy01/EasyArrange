package org.example.backend.DTO.Shift;

import org.example.backend.Model.entity.Shift;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ShiftResponseMapper implements Function<Shift, ShiftResponse> {
    @Override
    public ShiftResponse apply(Shift shift) {
        return new ShiftResponse(
                shift.getId(),
                shift.getDay(),
                shift.getStartShift(),
                shift.getEndShift()
        );
    }
}

