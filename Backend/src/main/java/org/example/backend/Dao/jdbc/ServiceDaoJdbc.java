package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.ServiceDao;
import org.example.backend.Entities.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaoJdbc extends JdbcConnection implements ServiceDao {
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
                    rs.getInt("price"),
                    rs.getInt("duration")
            );
        }
        return null;
    }

    @Override
    public void create(Service object) throws SQLException {

    }

    @Override
    public void update(Service service) throws SQLException {
        String sql = "UPDATE service SET name = ?, price = ?, duration = ? WHERE id = ?;";
        PreparedStatement update = connection.prepareStatement(sql);
        update.setString(1,service.getName());
        update.setInt(2,service.getPrice());
        update.setLong(3,service.getId());

        update.executeUpdate();
    }

    @Override
    public void remove(Service object) throws SQLException {

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
                    rs.getInt("price"),
                    rs.getInt("duration")
            ));
        }
        return services;
    }
}
