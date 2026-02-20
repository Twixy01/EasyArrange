package org.example.backend.Dao.jpa;

import org.example.backend.Dao.UserDao;
import org.example.backend.Model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;

public class UserDaoJPA implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findUser(String email, String password) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE email =: email AND password =: password", User.class);
            q.setParameter("email", email);
            q.setParameter("password", password);
            return q.uniqueResult();
        }

    }

    @Override
    public List<User> findUsersByRoleName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE User.role.name like :roleName", User.class);
            q.setParameter("roleName", roleName);
            return q.getResultList();
        }
    }

    @Override
    public List<User> findAllStaff() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE User.role =: admin", User.class);
            return q.getResultList();
        }
    }

    @Override
    public List<User> findAllCustomer() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE User.role =: customer", User.class);
            return q.getResultList();

        }
    }

    @Override
    public List<User> searchUsersByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE name like :name", User.class);
            q.setParameter("name", "%" + name + "%");
            return q.getResultList();
        }
    }


    @Override
    public User findUserById(long user_id) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE id =: user_id", User.class);
            q.setParameter("user_id", "%" + user_id + "%");
            return q.uniqueResult();

        }
    }

    @Override
    public boolean emailExists(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User WHERE email =: email", User.class);
            q.setParameter("email", email);
            return q.uniqueResult() != null;
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(User object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("FROM User ", User.class);
            return q.getResultList();
        }
    }
}


