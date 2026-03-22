package org.example.backend.Controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.Service.ServiceRequest;
import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.Service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@Validated
public class ServiceRestController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceRestController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<ServiceResponse> getServices(){
        return serviceService.findAll();
    }

    @GetMapping("/ordered")
    public List<ServiceResponse> getServicesOrderedByName(){
        return serviceService.findAllOrderedByName();
    }

    @GetMapping("/{serviceId}")
    public ServiceResponse getServiceById(@PathVariable Long serviceId) {
        return serviceService.findById(serviceId);
    }

    @GetMapping("/name/{serviceName}")
    public ServiceResponse getServiceByName(@PathVariable String serviceName) {
        return serviceService.findByName(serviceName);
    }

    @PostMapping("/create")
    public ServiceResponse createService(@Valid @RequestBody ServiceRequest serviceDto) {
        return serviceService.create(serviceDto);
    }

    @PutMapping("/{serviceId}")
    public ServiceResponse updateService(@PathVariable Long serviceId, @Valid @RequestBody ServiceRequest serviceDto) {
        return serviceService.update(serviceId, serviceDto);
    }

    @DeleteMapping("/{serviceId}")
    public void deleteService(@PathVariable Long serviceId) {
        serviceService.remove(serviceId);
    }
}
