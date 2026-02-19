package org.example.backend.Config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@WebListener
public class HibernateContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            sce.getServletContext().setAttribute("sessionFactory", sessionFactory);
            System.out.println("Hibernate SessionFactory sikeresen elindult.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Nem sikerült a Hibernate inicializálása!");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactory sessionFactory = (SessionFactory) sce.getServletContext().getAttribute("sessionFactory");
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory lezárva.");
        }


    }
}