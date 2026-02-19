package org.example.backend.Dao;

import java.sql.SQLException;

public interface ServiceDao extends Dao<Service>{
    Service findServiceById(long serviceId) throws SQLException;
    Service readServiceByName(String serviceName) throws SQLException;
    boolean serviceExists(String service) throws SQLException;
}
