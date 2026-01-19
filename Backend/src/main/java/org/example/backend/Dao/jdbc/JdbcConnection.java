package org.example.backend.Dao.jdbc;

import java.sql.Connection;

public abstract class JdbcConnection{
    protected final Connection connection;

    protected JdbcConnection(Connection connection) {
        this.connection = connection;
    }
}
