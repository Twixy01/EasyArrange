package org.example.backend.Dao.jpa;

import org.example.backend.Dao.StaffShiftDao;
import org.example.backend.Model.entity.Shift;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffShift;
import org.hibernate.SessionFactory;

import java.util.List;

public class StaffShiftDaoJPA implements StaffShiftDao{
    private final SessionFactory sessionFactory;

    public StaffShiftDaoJPA(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Shift> findAllShiftsByStaffId(long staffId) {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "FROM StaffShift ss WHERE ss.staff.id = :staffId",
                    Shift.class);
            query.setParameter("staffId", staffId);
            return query.list();
        }
    }

    @Override
    public List<Staff> findAllStaffByShiftId(long shiftId) {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "FROM StaffShift ss WHERE ss.shift.id = :shiftId",
                    Staff.class);
            query.setParameter("shiftId", shiftId);
            return query.list();
        }
    }

    @Override
    public StaffShift findById(long id) {
        try(var session = sessionFactory.openSession()){
            return session.get(StaffShift.class, id);
        }
    }

    @Override
    public void create(StaffShift object) {
        try(var session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(StaffShift object) {
        try(var session = sessionFactory.openSession()){
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(StaffShift object) {
        try(var session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(object);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<StaffShift> findAll() {
        try(var session = sessionFactory.openSession()) {
            return session.createQuery("FROM StaffShift", StaffShift.class).list();
        }
    }
}
