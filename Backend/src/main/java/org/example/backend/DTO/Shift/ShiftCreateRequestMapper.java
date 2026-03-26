package org.example.backend.DTO.Shift;

import org.example.backend.Model.entity.Shift;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ShiftCreateRequestMapper implements Function<ShiftCreateRequest, Shift> {
    @Override
    public Shift apply(ShiftCreateRequest request) {
        Shift shift = new Shift();
        // we set it in the service layer
        shift.setStartShift(request.startShift());
        shift.setEndShift(request.endShift());
        return shift;
    }
}
