package org.example.backend.DTO.Shift;

import org.example.backend.Model.entity.Shift;

import java.util.function.Function;

public class ShiftCreateRequestMapper implements Function<Shift, ShiftCreateRequest> {
    @Override
    public ShiftCreateRequest apply(Shift shift) {
        return new ShiftCreateRequest(
                shift.getStartShift(),
                shift.getEndShift()
        );
    }
}
