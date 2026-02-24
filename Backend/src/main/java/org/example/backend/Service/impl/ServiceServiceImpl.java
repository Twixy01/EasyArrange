package org.example.backend.Service.impl;

import org.example.backend.Dao.ServiceDao;
import org.example.backend.Model.entity.Service;
import org.example.backend.Service.ServiceService;

import java.util.List;

public class ServiceServiceImpl implements ServiceService {
    private final ServiceDao serviceDao;

    public ServiceServiceImpl(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Override
    public List<Service> getAllServices() {
        return List.of();
    }

    @Override
    public Service getServiceByName(String name) {
        return null;
    }

}
