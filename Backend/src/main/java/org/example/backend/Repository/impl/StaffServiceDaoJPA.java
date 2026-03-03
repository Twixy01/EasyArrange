package org.example.backend.Repository.impl;

import org.example.backend.Repository.StaffServiceDao;
import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class StaffServiceDaoJPA implements StaffServiceDao {
    private final SessionFactory sessionFactory;

    public StaffServiceDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Service> findAllServicesByStaffId(long staffId) {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "FROM StaffService ss WHERE ss.staff.id = :staffId",
                    Service.class);
            query.setParameter("staffId", staffId);
            return query.list();
        }
    }

    @Override
    public List<Staff> findAllStaffByServiceId(long serviceId) {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "FROM StaffService ss WHERE ss.service.id = :serviceId",
                    Staff.class);
            query.setParameter("serviceId", serviceId);
            return query.list();
        }
    }

    @Override
    public StaffService findById(long id) {
        try(Session session = sessionFactory.openSession()){
            return session.get(StaffService.class, id);
        }
    }

    @Override
    public boolean create(StaffService staffService) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(staffService);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(StaffService staffService) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.update(staffService);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(StaffService staffService) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(staffService);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<StaffService> findAll() {
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("FROM StaffService",StaffService.class).list();
        }
    }
}
