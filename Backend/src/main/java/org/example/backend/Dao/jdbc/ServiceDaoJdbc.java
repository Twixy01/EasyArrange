package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Entities.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaoJdbc extends JdbcConnection implements ServiceDao<Service> {
    public ServiceDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public Service findServiceById(long serviceId) throws SQLException {
        PreparedStatement serviceById = connection.prepareStatement("SELECT * FROM service WHERE id = ?");
        serviceById.setLong(1,serviceId);
        ResultSet rs = serviceById.executeQuery();

        if (rs.next()){
            return new Service(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getLong("duration")
            );
        }
        return null;
    }

    @Override
    public List<Service> findAll() throws SQLException{
        List<Service> services = new ArrayList<>();
        Statement findAll = connection.createStatement();
        ResultSet rs = findAll.executeQuery("SELECT * FROM service");
        while (rs.next()){
            services.add(new Service(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getLong("duration")
            ));
        }
        return services;
    }
}
