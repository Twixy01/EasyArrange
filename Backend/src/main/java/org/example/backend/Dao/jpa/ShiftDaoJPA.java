package org.example.backend.Dao.jpa;

import org.example.backend.Dao.ShiftDao;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Model.entity.Booking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.awt.print.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class ShiftDaoJPA implements ShiftDao {
    private final SessionFactory sessionFactory;

    public ShiftDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Shift> findAllShiftsByStaffId(int staffId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift WHERE staff.id = :staffId", Shift.class);
            q.setParameter("staffId", staffId);
            return q.getResultList();
        }
    }

    @Override
    public List<Shift> findAllShiftsByDate(LocalTime date) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift WHERE startShift = :date", Shift.class);
            q.setParameter("date", date);
            return q.getResultList();
        }
    }

    @Override
    public List<Shift> findShiftsBetweenDates(LocalTime startTime, LocalTime endTime) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift WHERE startShift >= :startTime AND endShift <= :endTime", Shift.class);
            q.setParameter("startDate", startTime);
            q.setParameter("endDate", endTime);
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

    @Override
    public List<Shift> findShiftsByStaffIdBetweenDates(int staffId, LocalTime startTime, LocalTime endTime) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shift> q = session.createQuery("FROM Shift WHERE staff.id = :staffId AND startShift >= :startDate AND endShift <= :endDate", Shift.class);
            q.setParameter("staffId", staffId);
            q.setParameter("startDate", startTime);
            q.setParameter("endDate", endTime);
            return q.getResultList();
        }
    }

}
