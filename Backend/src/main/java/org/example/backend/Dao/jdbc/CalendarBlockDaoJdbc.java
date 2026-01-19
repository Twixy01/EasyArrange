package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.CalendarBlockDao;
import org.example.backend.Entities.CalendarBlock;

import java.sql.Connection;

public class CalendarBlockDaoJdbc extends JdbcConnection implements CalendarBlockDao<CalendarBlock> {
    protected CalendarBlockDaoJdbc(Connection connection) {
        super(connection);
    }
}
