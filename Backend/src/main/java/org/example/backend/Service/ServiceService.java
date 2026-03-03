package org.example.backend.Service;

import org.example.backend.Model.entity.Service;

public interface ServiceService {
    Service getServiceByName(String serviceName);
    Service getServiceById(int serviceId);
}
