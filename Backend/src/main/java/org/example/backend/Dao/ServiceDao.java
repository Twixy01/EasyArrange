package org.example.backend.Dao;

import org.example.backend.Model.entity.Service;

public interface ServiceDao extends Dao<Service>{
    Service findServiceById(long serviceId);
    Service readServiceByName(String serviceName);
    boolean serviceExists(String service);
}
