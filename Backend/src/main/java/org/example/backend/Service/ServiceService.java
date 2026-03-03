package org.example.backend.Service;

import org.example.backend.Model.entity.Service;

import java.util.List;

public interface ServiceService {
    Service getServiceByName(String serviceName);
    Service getServiceById(int serviceId);
}
