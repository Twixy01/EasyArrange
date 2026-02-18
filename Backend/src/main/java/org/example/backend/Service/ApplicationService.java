package org.example.backend.Service;

import java.sql.Connection;
import java.sql.SQLException;

import org.example.backend.Service.BookingService;
import org.example.backend.Service.UserService;
import org.example.backend.Service.CalendarBlockService;
import org.example.backend.Service.Service;

public class ApplicationService {
    private final Connection connection;
    private final BookingService bookingService;
    private final UserService userService;
    private final Service serviceService; // service-layer class is named Service in this package
    private final CalendarBlockService calendarBlockService;

    public ApplicationService(Connection connection) {
        this.connection = connection;
        this.bookingService = new BookingService(connection);
        this.userService = new UserService(connection);
        this.serviceService = new Service(connection);
        this.calendarBlockService = new CalendarBlockService(connection);
    }

    public BookingService booking() { return bookingService; }
    public UserService user() { return userService; }
    public Service service() { return serviceService; }
    public CalendarBlockService calendar() { return calendarBlockService; }

    @FunctionalInterface
    public interface TransactionalOperation<T> {
        T run() throws SQLException;
    }

    public <T> T runInTransaction(TransactionalOperation<T> op) throws SQLException {
        boolean originalAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            T result = op.run();
            connection.commit();
            return result;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* swallow or log if available */ }
            throw e;
        } catch (RuntimeException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* swallow or log if available */ }
            throw e;
        } finally {
            try { connection.setAutoCommit(originalAutoCommit); } catch (SQLException ex) { /* swallow */ }
        }
    }


    public void runInTransactionVoid(TransactionalOperation<Void> op) throws SQLException {
        runInTransaction(op);
    }
}
