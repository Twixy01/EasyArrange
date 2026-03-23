package org.example.backend.DTO.StaffService;

import org.example.backend.Model.entity.StaffServiceJunction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffServiceResponseMapper implements Function<StaffServiceJunction, StaffServiceResponse> {
    @Override
    public StaffServiceResponse apply(StaffServiceJunction staffServiceJunction) {
        return new StaffServiceResponse(
                staffServiceJunction.getStaff().getId(),
                staffServiceJunction.getService().getId()
        );
    }
}
