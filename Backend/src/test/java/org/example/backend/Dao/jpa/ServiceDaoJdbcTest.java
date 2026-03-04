//package org.example.backend.Dao.jpa;
//
//
//import org.example.backend.Dao.ServiceDao;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ServiceDaoJdbcTest {
//
//    Connection conn;
//    ServiceDao model;
//
//    @BeforeEach
//    void setUp() throws SQLException {
//        try {
//            Class.forName("org.mariadb.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        conn = DriverManager.getConnection(
//                "jdbc:mariadb://localhost:3306/test_easyarrange", "root", ""
//        );
//        model = new ServiceDaoJPA(conn) {
//        };
//    }
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        if (conn != null && !conn.isClosed()) {
//            conn.close();
//        }
//    }
//
//    @Test
//    void successfullyCreatedService() throws SQLException {
//        Service service = new Service("Test Service", 100, 60);
//        Service created = null;
//        try {
//            model.create(service);
//            created = model.findServiceById(service.getService_id());
//            assertNotNull(created, "Service should be created and found in the database");
//
//            assertEquals(service.getService_id(), created.getService_id());
//            assertEquals(service.getName(), created.getName());
//            assertEquals(service.getPrice(), created.getPrice());
//            assertEquals(service.getDuration(), created.getDuration());
//        } finally {
//            if (created != null) model.remove(created);
//        }
//        created = model.findServiceById(service.getService_id());
//        assertNull(created);
//    }
//
//    @Test
//    void throwsExceptionWhenCreatingDuplicateService() throws SQLException {
//        Service service = new Service("Test Service", 100, 60);
//        Service created = null;
//        try {
//            model.create(service);
//            created = model.findServiceById(service.getService_id());
//            IllegalArgumentException thrownException = assertThrows(
//                    IllegalArgumentException.class,
//                    () -> model.create(service)
//            );
//
//            assertEquals("Service already exists.", thrownException.getMessage());
//        } finally {
//            if (created != null) model.remove(created);
//        }
//        created = model.findServiceById(service.getService_id());
//        assertNull(created);
//    }
//
//    @Test
//    void remove_worksAsIntended() throws SQLException {
//        Service service = new Service("Manikűr", 6500, 45);
//        ServiceDao serviceDao = new ServiceDaoJPA(conn);
//
//        serviceDao.create(service);
//
//        Service createdService = serviceDao.findServiceById(service.getService_id());
//        assertNotNull(createdService, "Service should exist before removal");
//
//        serviceDao.remove(createdService);
//
//        Service deletedService = serviceDao.readServiceByName("Manikűr");
//        assertNull(deletedService, "Service should no longer exist in the database after removal");
//    }
//
//    @Test
//    void update_worksAsIntended() throws SQLException {
//        Service service = new Service("Pedikűr", 8000, 60);
//        ServiceDao serviceDao = new ServiceDaoJPA(conn);
//
//        serviceDao.create(service);
//
//        Service createdService = serviceDao.findServiceById(service.getService_id());
//        assertNotNull(createdService, "Service should exist before update");
//
//        createdService.setPrice(8500);
//        createdService.setDuration(75);
//        serviceDao.update(createdService);
//
//        Service updatedService = serviceDao.findServiceById(service.getService_id());
//        assertNotNull(updatedService, "Updated service should still exist");
//        assertEquals(8500, updatedService.getPrice(), "Price should be updated to 8500");
//        assertEquals(75, updatedService.getDuration(), "Duration should be updated to 75 minutes");
//        serviceDao.remove(service);
//    }
//
//    @Test
//    void readServiceById_worksAsIntended() throws SQLException {
//        Service service = new Service("Szempilla lifting", 12000, 90);
//        ServiceDao serviceDao = new ServiceDaoJPA(conn);
//
//        serviceDao.create(service);
//
//        Service foundService = serviceDao.findServiceById(service.getService_id());
//        assertNotNull(foundService, "Service should be found by ID");
//        assertEquals("Szempilla lifting", foundService.getName(), "Service name should match");
//        assertEquals(12000, foundService.getPrice(), "Service price should match");
//        assertEquals(90, foundService.getDuration(), "Service duration should match");
//        serviceDao.remove(service);
//    }
//}