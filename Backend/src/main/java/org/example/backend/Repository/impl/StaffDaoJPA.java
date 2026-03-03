package org.example.backend.Repository.impl;

import org.example.backend.Repository.StaffDao;
import org.example.backend.Model.entity.Staff;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StaffDaoJPA implements StaffDao {
    private final SessionFactory sessionFactory;

    public StaffDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Staff findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Staff.class, id);
        }
    }

    @Override
    public boolean create(Staff staff) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(staff);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Staff staff) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(staff);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Staff staff) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(staff);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Staff> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Staff", Staff.class).list();
        }
    }

    @Override
    public Staff findByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Staff> query = session.createQuery("FROM Staff s WHERE s.user.id = :userId", Staff.class);
            query.setParameter("userId", userId);
            return query.uniqueResult();
        }
    }
}
