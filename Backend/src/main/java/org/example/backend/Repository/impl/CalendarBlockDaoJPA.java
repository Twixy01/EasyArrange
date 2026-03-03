package org.example.backend.Repository.impl;

import org.example.backend.Repository.CalendarBlockDao;
import org.example.backend.Model.entity.CalendarBlock;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class CalendarBlockDaoJPA implements CalendarBlockDao {
    private final SessionFactory sessionFactory;

    public CalendarBlockDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<CalendarBlock> findCalendarBlocksByStaffId(long staffId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CalendarBlock> q = session.createQuery("FROM CalendarBlock WHERE staff.id = :staffId", CalendarBlock.class);
            q.setParameter("staffId", staffId);
            return q.list();
        }
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksBetween(LocalDateTime start, LocalDateTime end) {
        try (Session session =  sessionFactory.openSession()) {
            Query<CalendarBlock> q = session.createQuery("FROM CalendarBlock WHERE startDatetime >=: start " +
                    "AND endDatetime <=: end", CalendarBlock.class);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    @Override
    public List<CalendarBlock> findCalendarBlocksByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<CalendarBlock> q = session.createQuery("FROM CalendarBlock WHERE staff.id = :staffId " +
                    "AND startDatetime >=: start AND endDatetime <=: end", CalendarBlock.class);
            q.setParameter("staffId", staffId);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    @Override
    public CalendarBlock findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<CalendarBlock> q = session.createQuery("FROM CalendarBlock WHERE id = :id", CalendarBlock.class);
            q.setParameter("id", id);
            return q.uniqueResult();
        }
    }

    @Override
    public boolean create(CalendarBlock object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(object);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(CalendarBlock object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean remove(CalendarBlock object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CalendarBlock> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<CalendarBlock> q = session.createQuery("FROM CalendarBlock", CalendarBlock.class);
            return q.list();
        }
    }

}
