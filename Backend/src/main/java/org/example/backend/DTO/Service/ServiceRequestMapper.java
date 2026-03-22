package org.example.backend.DTO.Service;

import org.example.backend.Model.entity.Service;

import java.util.function.BiConsumer;

@org.springframework.stereotype.Service
public class ServiceRequestMapper implements BiConsumer<ServiceRequest, Service> {

     @Override
    public void accept(ServiceRequest serviceRequest, Service service) {
        service.setName(serviceRequest.name());
        service.setPrice(serviceRequest.price());
        service.setDuration(serviceRequest.duration());
    }

}
