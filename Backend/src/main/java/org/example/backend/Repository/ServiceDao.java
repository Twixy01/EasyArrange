package org.example.backend.Repository;

import org.example.backend.Model.entity.Service;

public interface ServiceDao extends Dao<Service>{
    Service findServiceByName(String serviceName);
    boolean serviceExists(String service);
}
