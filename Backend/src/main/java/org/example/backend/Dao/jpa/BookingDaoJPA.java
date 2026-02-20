package org.example.backend.Dao.jpa;

import org.example.backend.Dao.BookingDao;
import org.example.backend.Model.entity.Booking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.awt.print.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDaoJPA implements BookingDao {
    private final SessionFactory sessionFactory;

    public BookingDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Booking findBookingById(long booking_id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE booking_id = :booking_id", Booking.class);
            q.setParameter("booking_id", booking_id);
            return q.uniqueResult();
        }

    }

    @Override
    public List<Booking> findBookingsByStaffId(long staffId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE staff_id = :staffId", Booking.class);
            q.setParameter("staffId", staffId);
            return q.list();
        }

    }

    @Override
    public List<Booking> findBookingsByCustomerId(long customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE customer_id =: customerId", Booking.class);
            q.setParameter("customerId", customerId);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsBetween(LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE start_datetime >=: start " +
                    "AND end_datetime <=: end", Booking.class);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsByStaffBetween(long staffId, LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE staff_id =: staffId " +)
            "AND start_datetime >=: start AND end_datetime <=: end", Booking.class);
            q.setParameter("staffId", staffId);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }

    @Override
    public List<Booking> findBookingsByCustomerBetween(long customerId, LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE customrer_id =: customerId " +
                    "AND start_datetime >=: start AND end_datetime <=: end", Booking.class);
            q.setParameter("customerId", customerId);
            q.setParameter("start", start);
            q.setParameter("end", end);
            return q.list();
        }
    }


    //Még nem fog működni mert nincs implementálva a StaffDaoJPA
    public boolean hasBookingConflict(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            Query<Booking> q = session.createQuery("FROM Booking WHERE staff_id =: staffId " +
                    "AND ((start_datetime < :end AND end_datetime > :start))", Booking.class);
            q.setParameter("staffId", booking.getStaffId());
            q.setParameter("start", booking.getStartDatetime());
            q.setParameter("end", booking.getEndDatetime());
            return !q.list().isEmpty();
        }
    }

    @Override
    public void create(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(booking);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Booking booking) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(booking);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Booking object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
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
