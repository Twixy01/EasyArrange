package org.example.backend.Service.impl;

import org.example.backend.Repository.ServiceDao;
import org.example.backend.Model.entity.Service;
import org.example.backend.Service.ServiceService;

public class ServiceServiceImpl implements ServiceService {
    private final ServiceDao serviceDao;

    public ServiceServiceImpl(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Override
    public Service getServiceByName(String serviceName) {
        return serviceDao.findServiceByName(serviceName);
    }

    @Override
    public Service getServiceById(int serviceId) {
        return serviceDao.findById(serviceId);
    }
}
