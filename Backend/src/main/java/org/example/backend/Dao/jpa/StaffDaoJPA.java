package org.example.backend.Dao.jpa;

import org.example.backend.Dao.StaffDao;
import org.example.backend.Model.entity.Staff;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

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
    public void create(Staff staff) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(staff);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Staff staff) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(staff);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Staff staff) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(staff);
            session.getTransaction().commit();
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
