package org.example.backend.DTO.StaffService;

import org.example.backend.DTO.Service.ServiceResponseMapper;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.Model.entity.StaffServiceJunction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StaffServiceResponseMapper implements Function<StaffServiceJunction, StaffServiceResponse> {
    private final StaffResponseMapper staffResponseMapper;
    private final ServiceResponseMapper serviceResponseMapper;

    public StaffServiceResponseMapper(StaffResponseMapper staffResponseMapper, ServiceResponseMapper serviceResponseMapper) {
        this.staffResponseMapper = staffResponseMapper;
        this.serviceResponseMapper = serviceResponseMapper;
    }

    @Override
    public StaffServiceResponse apply(StaffServiceJunction staffServiceJunction) {
        return new StaffServiceResponse(
                staffResponseMapper.apply(staffServiceJunction.getStaff()),
                serviceResponseMapper.apply(staffServiceJunction.getService())
        );
    }
}
