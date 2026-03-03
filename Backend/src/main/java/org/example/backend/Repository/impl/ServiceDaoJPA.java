package org.example.backend.Repository.impl;

import org.example.backend.Repository.ServiceDao;
import org.example.backend.Model.entity.Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ServiceDaoJPA implements ServiceDao {
    private final SessionFactory sessionFactory;

    public ServiceDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Service findById(long service_id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Service> q = session.createQuery("FROM Service WHERE id =: service_id", Service.class);
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
    public boolean create(Service service) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(service);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Service service) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(service);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Service object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(object);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Service> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Service> q = session.createQuery("FROM Service", Service.class);
            return q.list();
        }
    }

    @Override
    public Service findServiceByName(String serviceName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Service> q = session.createQuery("FROM Service WHERE name =: serviceName", Service.class);
            q.setParameter("serviceName", serviceName);
            return q.uniqueResult();
        }
    }

}
