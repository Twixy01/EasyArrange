package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.RoleDao;
import org.example.backend.Entities.Role;
import org.example.backend.Entities.Service;
import org.example.backend.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoJdbc extends JdbcConnection implements RoleDao<Role> {
    public RoleDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public long findRoleIdByName(String roleName) throws SQLException {
        PreparedStatement roleIdByName = connection.prepareStatement("SELECT id FROM role WHERE name = ?");
        roleIdByName.setString(1,roleName);
        ResultSet rs = roleIdByName.executeQuery();

        if (rs.next()){
            return rs.getLong(1);
        }
        return -1;
    }

    @Override
    public String findRoleNameById(long roleId) throws SQLException {
        PreparedStatement getRoleName = connection.prepareStatement("SELECT name FROM role WHERE id = ?");
        getRoleName.setLong(1,roleId);
        ResultSet rs = getRoleName.executeQuery();
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public List<Role> findAll() throws SQLException{
        List<Role> roles = new ArrayList<>();
        Statement findAll = connection.createStatement();
        ResultSet rs = findAll.executeQuery("SELECT * FROM role");
        while (rs.next()){
            roles.add(new Role(
                    rs.getLong("id"),
                    rs.getString("name")
            ));
        }
        return roles;
    }
}
