package org.example.backend.DTO.Service;

import org.example.backend.Model.entity.Service;

import java.util.function.Function;

@org.springframework.stereotype.Service
public class ServiceResponseMapper implements Function<Service, ServiceResponse> {
    @Override
    public ServiceResponse apply(Service service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getPrice(),
                service.getDuration()
        );
    }
}
