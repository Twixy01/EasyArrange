package org.example.backend.Dao.jpa;

import org.example.backend.Dao.RoleDao;
import org.example.backend.Model.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.util.List;

public class RoleDaoJPA implements RoleDao {
    private final SessionFactory sessionFactory;

    public RoleDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long findRoleIdByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            Role role = (Role) session.createQuery("FROM Role WHERE name = :name")
                    .setParameter("name", roleName).getSingleResult();
            if (role != null) {
                return role.getId();
            } else {
                throw new IllegalArgumentException("Role id is not found by name");
            }
        }
    }

    @Override
    public Role findById(long roleId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Role.class, roleId);
        }
    }

    //
    @Override
    public void create(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(role);
            session.getTransaction().commit();
        }
    }

    //
    @Override
    public void update(Role role) {
        try (Session session = sessionFactory.openSession()) {
            Role targetRole = session.get(Role.class, role.getId());
            if (targetRole != null) {
                session.beginTransaction();
                targetRole.setName(role.getName());
                session.update(targetRole);
                session.getTransaction().commit();
            } else {
                throw new RuntimeException("Role with id not found.");
            }
        }
    }

    @Override
    public void remove(Role role) {
        try (Session session = sessionFactory.openSession()) {
            Role targetRole = session.get(Role.class, role.getId());
            if (targetRole != null) {
                session.beginTransaction();
                session.delete(targetRole);
                session.getTransaction().commit();
            } else {
                throw new RuntimeException("Role with id not found.");
            }
        }
    }

    @Override
    public List<Role> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Role", Role.class).list();
        }
    }
}
