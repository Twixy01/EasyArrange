package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.BookingDao;
import org.example.backend.Entities.Booking;

import java.sql.Connection;

public class BookingDaoJdbc extends JdbcConnection implements BookingDao<Booking> {
    protected BookingDaoJdbc(Connection connection) {
        super(connection);
    }
}
