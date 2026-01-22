package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Entities.Service;

import java.sql.Connection;

public class ServiceDaoJdbc extends JdbcConnection implements ServiceDao<Service> {
    public ServiceDaoJdbc(Connection connection) {
        super(connection);
    }
}
