package org.example.backend.Service;

import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Dao.jdbc.ServiceDaoJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Service {
    private final ServiceDao serviceDao;

    public Service(Connection connection) {
        this.serviceDao = new ServiceDaoJdbc(connection);
    }


    public org.example.backend.Entities.Service createService(org.example.backend.Entities.Service service) throws SQLException {
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        if (service.getName() == null || service.getName().trim().isEmpty())
            throw new IllegalArgumentException("Service name is required");

        if (serviceDao.serviceExists(service.getName())) {
            throw new IllegalArgumentException("Service already exists");
        }

        serviceDao.create(service);
        return service;
    }


    public void updateService(org.example.backend.Entities.Service service) throws SQLException {
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        if (serviceDao.findServiceById(service.getId()) == null) {
            throw new IllegalArgumentException("Service not found");
        }
        serviceDao.update(service);
    }


    public void deleteService(long id) throws SQLException {
        org.example.backend.Entities.Service existing = serviceDao.findServiceById(id);
        if (existing == null) throw new IllegalArgumentException("Service not found");
        serviceDao.remove(existing);
    }


    public org.example.backend.Entities.Service getServiceById(long id) throws SQLException {
        return serviceDao.findServiceById(id);
    }


    public org.example.backend.Entities.Service getServiceByName(String name) throws SQLException {
        return serviceDao.readServiceByName(name);
    }


    public List<org.example.backend.Entities.Service> listAllServices() throws SQLException {
        return serviceDao.findAll();
    }
}

