package org.example.backend.Repository.impl;

import org.example.backend.Repository.BookingDao;
import org.example.backend.Model.entity.Booking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class BookingDaoJPA implements BookingDao {
    private final SessionFactory sessionFactory;

    public BookingDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Booking findById(long bookingId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE id = :bookingId", Booking.class);
            q.setParameter("bookingId", bookingId);
            return q.uniqueResult();
        }
    }

    @Override
    public List<Booking> findBookingsByStaffId(long staffId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE staff.id = :staffId", Booking.class);
            q.setParameter("staffId", staffId);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsByCustomerId(long customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE customer.id =: customerId", Booking.class);
            q.setParameter("customerId", customerId);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE startDatetime >=: start " +
                    "AND endDatetime <=: end", Booking.class);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE staff.id =: staffId " +
                    "AND startDatetime >=: start AND endDatetime <=: end", Booking.class);
            q.setParameter("staffId", staffId);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsByCustomerBetween(long customerId, LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE customer.id =: customerId " +
                    "AND startDatetime >=: start AND endDatetime <=: end", Booking.class);
            q.setParameter("customerId", customerId);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    public boolean hasBookingConflict(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE staff.id =: staffId " +
                    "AND ((startDatetime < :end AND endDatetime > :start))", Booking.class);
            q.setParameter("staffId", booking.getStaff().getId());
            q.setParameter("start", booking.getStartDatetime());
            q.setParameter("end", booking.getEndDatetime());
            return !q.list().isEmpty();
        }
    }

    @Override
    public boolean create(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(booking);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(booking);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Booking object) {
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
    public List<Booking> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking", Booking.class);
            return q.list();
        }
    }
}
