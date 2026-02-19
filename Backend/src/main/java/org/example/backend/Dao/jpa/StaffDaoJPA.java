package org.example.backend.Dao.jpa;

import org.example.backend.Dao.StaffDao;
import org.hibernate.SessionFactory;

public class StaffDaoJPA implements StaffDao {
    private final SessionFactory sessionFactory;

    public StaffDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
