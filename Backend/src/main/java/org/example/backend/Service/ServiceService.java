package org.example.backend.Service;

import org.example.backend.Model.entity.Service;

import java.util.List;

public interface ServiceService {

        List<Service> getAllServices();
        Service getServiceByName(String name);
}
