package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.Service.ServiceRequest;
import org.example.backend.DTO.Service.ServiceRequestMapper;
import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.Service.ServiceResponseMapper;
import org.example.backend.Model.entity.Service;
import org.example.backend.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceRequestMapper serviceRequestMapper;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ServiceResponseMapper serviceResponseMapper, ServiceRequestMapper serviceRequestMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceResponseMapper = serviceResponseMapper;
        this.serviceRequestMapper = serviceRequestMapper;
    }

    public List<ServiceResponse> findAll() {
        return serviceRepository.findAll().stream()
                .map(serviceResponseMapper)
                .collect(Collectors.toList());
    }

    public List<ServiceResponse> findAllOrderedByName() {
        return serviceRepository.findAllByOrderByNameAsc().stream()
                .map(serviceResponseMapper)
                .collect(Collectors.toList());
    }

    public ServiceResponse findById(Long id) {
        Optional<Service> service = serviceRepository.findById(id);

        return service.map(serviceResponseMapper).orElseThrow(() ->
                new IllegalArgumentException("Service not found with id: " + id));
    }

    public ServiceResponse findByName(String name) {
        Optional<Service> service = serviceRepository.findByName(name);
        return service.map(serviceResponseMapper).orElseThrow(() ->
                new IllegalArgumentException("Service not found with name: " + name));
    }

    public boolean serviceExists(String serviceName) {
        return serviceRepository.existsByName(serviceName);
    }

    @Transactional
    public ServiceResponse create(ServiceRequest serviceDto) {
        if (serviceExists(serviceDto.name())) {
            throw new IllegalArgumentException("Service with name '" + serviceDto.name() + "' already exists.");
        }

        Service service = new Service();
        serviceRequestMapper.accept(serviceDto, service);

        serviceRepository.save(service);

        return serviceResponseMapper.apply(service);
    }

    @Transactional
    public ServiceResponse update(Long serviceId, ServiceRequest serviceDto) {
        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + serviceId));

        if (!existingService.getName().equals(serviceDto.name()) && serviceRepository.existsByName(serviceDto.name())){
            throw new IllegalArgumentException("Service with name '" + serviceDto.name() + "' already exists.");
        }

        serviceRequestMapper.accept(serviceDto, existingService);

        serviceRepository.save(existingService);

        return serviceResponseMapper.apply(existingService);
    }

    @Transactional
    public void remove(Long id) {
        serviceRepository.deleteById(id);
    }
}
