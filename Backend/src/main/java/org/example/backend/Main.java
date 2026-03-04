package org.example.backend;

import org.example.backend.Dao.jpa.ShiftDaoJPA;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()){
            ShiftDaoJPA shiftDaoJPA = new ShiftDaoJPA(sessionFactory);
            shiftDaoJPA.findAllShiftsByTime(LocalTime.of(15,30)).forEach(shift -> System.out.println(shift));
        }
    }
}
