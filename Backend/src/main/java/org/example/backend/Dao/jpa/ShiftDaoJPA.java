package org.example.backend.Dao.jpa;

import org.example.backend.Dao.ShiftDao;
import org.example.backend.Model.entity.Shift;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalTime;
import java.util.List;

public class ShiftDaoJPA implements ShiftDao {
    private final SessionFactory sessionFactory;

    public ShiftDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Shift> getAllShiftsByStaffId(int staffId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift WHERE staff.id = :staffId", Shift.class);
            q.setParameter("staffId", staffId);
            return q.getResultList();
        }
    }

    @Override
    public List<Shift> getAllShiftsByTime(LocalTime time) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery(
                    "FROM Shift WHERE startShift <= :time AND endShift >= :time", Shift.class
            );
            q.setParameter("time", time);
            return q.getResultList();
        }
    }

    @Override
    public List<Shift> findShiftsBetweenTime(LocalTime startTime, LocalTime endDate) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift WHERE startShift >= :startTime AND endShift <= :endDate", Shift.class);
            q.setParameter("startTime", startTime);
            q.setParameter("endDate", endDate);
            return q.getResultList();
        }
    }

    @Override
    public Shift findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Shift.class, id);
        }
    }

    @Override
    public void create(Shift object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Shift object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Shift object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Shift> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift", Shift.class);
            return q.getResultList();
        }
    }
}
