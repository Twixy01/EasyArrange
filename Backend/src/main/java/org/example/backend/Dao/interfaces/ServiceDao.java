package org.example.backend.Dao.interfaces;

import org.example.backend.Entities.Service;

import java.sql.SQLException;
import java.util.List;

public interface ServiceDao extends Dao<Service>{
    Service findServiceById(long serviceId) throws SQLException;
    Service readServiceByName(String serviceName) throws SQLException;
    boolean serviceExists(String service) throws SQLException;
}
