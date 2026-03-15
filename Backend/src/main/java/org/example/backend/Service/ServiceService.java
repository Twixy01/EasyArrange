package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.backend.Model.entity.Service;
import org.example.backend.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public List<Service> findAllOrderedByName() {
        return serviceRepository.findAllByOrderByNameAsc();
    }

    public boolean serviceExists(String service) {
        return serviceRepository.existsByName(service);
    }

    public Service findById(Long id) {
        Optional<Service> service = serviceRepository.findById(id);

        return service.orElseThrow(()->
                new IllegalArgumentException("Service not found with id: " + id));
    }

    public Service findByName(String name) {
        return serviceRepository.findByName(name).orElseThrow(() ->
                new IllegalArgumentException("Service not found with name: " + name));
    }

    @Transactional
    public Service create(@NotNull @Valid Service service) {
        return serviceRepository.save(service);
    }

    @Transactional
    public Service update(@NotNull @Valid Service service) {
        return serviceRepository.save(service);
    }

    @Transactional
    public void remove(Long id) {
        serviceRepository.deleteById(id);
    }
}
