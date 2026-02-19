package org.example.backend.Dao.jpa;

import org.example.backend.Dao.ServiceDao;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaoJPA implements ServiceDao {
    private final SessionFactory sessionFactory;

    public ServiceDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//
//    @Override
//    public Service findServiceById(long service_id) throws SQLException {
//        PreparedStatement serviceById = connection.prepareStatement("SELECT * FROM service WHERE service_id = ?");
//        serviceById.setLong(1, service_id);
//        ResultSet rs = serviceById.executeQuery();
//
//        if (rs.next()) {
//            return new Service(
//                    rs.getLong("service_id"),
//                    rs.getString("name"),
//                    rs.getInt("price"),
//                    rs.getInt("duration")
//            );
//        }
//        return null;
//    }
//
//    @Override
//    public boolean serviceExists(String service) throws SQLException {
//        if (service == null || service.trim().isEmpty()) {
//            return false;
//        }
//        PreparedStatement findServiceName = connection.prepareStatement("SELECT name FROM service WHERE name = ? LIMIT 1");
//        findServiceName.setString(1, service);
//        ResultSet rs = findServiceName.executeQuery();
//
//        return rs.next();
//    }
//
//    @Override
//    public void create(Service service) throws SQLException {
//        if (serviceExists(service.getName())){
//            throw new IllegalArgumentException("Service already exists.");
//        }
//
//        String sql = "INSERT INTO service (name, price, duration) VALUES (?, ?, ?)";
//        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//
//        stmt.setString(1, service.getName());
//        stmt.setInt(2, service.getPrice());
//        stmt.setInt(3, service.getDuration());
//
//        stmt.executeUpdate();
//
//        ResultSet keys = stmt.getGeneratedKeys();
//        if (keys.next()) {
//            service.setService_id(keys.getLong(1));
//        }
//    }
//
//
//    @Override
//    public void update(Service service) throws SQLException {
//        String sql = "UPDATE service SET name = ?, price = ?, duration = ? WHERE service_id = ?;";
//        PreparedStatement update = connection.prepareStatement(sql);
//        update.setString(1,service.getName());
//        update.setInt(2,service.getPrice());
//        update.setInt(3, service.getDuration());
//        update.setLong(4, service.getService_id());
//
//        update.executeUpdate();
//    }
//
//    @Override
//    public void remove(Service object) throws SQLException {
//        String sql = "DELETE FROM service WHERE id = ?;";
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        stmt.setLong(1, object.getService_id());
//
//        stmt.executeUpdate();
//
//    }
//
//    @Override
//    public List<Service> findAll() throws SQLException {
//        List<Service> services = new ArrayList<>();
//        Statement findAll = connection.createStatement();
//        ResultSet rs = findAll.executeQuery("SELECT * FROM service");
//        while (rs.next()) {
//            services.add(new Service(
//                    rs.getLong("service_id"),
//                    rs.getString("name"),
//                    rs.getInt("price"),
//                    rs.getInt("duration")
//            ));
//        }
//        return services;
//    }
//
//    @Override
//    public Service readServiceByName(String serviceName) throws SQLException {
//        PreparedStatement serviceByName = connection.prepareStatement("SELECT * FROM service WHERE name = ?");
//        serviceByName.setString(1,serviceName);
//        ResultSet rs = serviceByName.executeQuery();
//
//        if (rs.next()){
//            return new Service(
//                    rs.getLong("service_id"),
//                    rs.getString("name"),
//                    rs.getInt("price"),
//                    rs.getInt("duration")
//            );
//        }
//        return null;
//    }
}
