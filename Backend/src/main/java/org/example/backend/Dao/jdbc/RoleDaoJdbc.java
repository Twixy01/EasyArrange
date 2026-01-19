package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.RoleDao;
import org.example.backend.Entities.Role;

import java.sql.Connection;

public class RoleDaoJdbc extends JdbcConnection implements RoleDao<Role> {
    protected RoleDaoJdbc(Connection connection) {
        super(connection);
    }
}
