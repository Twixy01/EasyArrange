package org.example.backend.Dao.jpa;

import org.example.backend.Dao.ServiceDao;
import org.example.backend.Model.entity.Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaoJPA implements ServiceDao {
    private final SessionFactory sessionFactory;

    public ServiceDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Service findServiceById(long service_id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Service> q = session.createQuery("FROM Service WHERE service_id =: service_id", Service.class);
            q.setParameter("service_id", service_id);
            return q.uniqueResult();
        }
    }

    @Override
    public boolean serviceExists(String service) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> q = session.createQuery("SELECT COUNT(*) FROM Service WHERE name =: service", Long.class);
            q.setParameter("service", service);
            return q.uniqueResult() > 0;
        }
    }

    @Override
    public void create(Service service) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Service service) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Service object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(object);
            session.getTransaction().commit();
        }

    }

    @Override
    public List<Service> findAll(){
        try (Session session = sessionFactory.openSession()) {
            Query<Service> q = session.createQuery("FROM Service", Service.class);
            return q.list();
        }
    }

    @Override
    public Service readServiceByName(String serviceName) {
       try (Session session = sessionFactory.openSession()) {
            Query<Service> q = session.createQuery("FROM Service WHERE name =: serviceName", Service.class);
            q.setParameter("serviceName", serviceName);
            return q.uniqueResult();
       }
    }

}
