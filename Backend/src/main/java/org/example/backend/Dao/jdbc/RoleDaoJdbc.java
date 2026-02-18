package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.RoleDao;
import org.example.backend.Entities.CalendarBlock;
import org.example.backend.Entities.Role;
import org.example.backend.Entities.Service;
import org.example.backend.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoJdbc extends JdbcConnection implements RoleDao {
    public RoleDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public long findRoleIdByName(String roleName) throws SQLException {
        PreparedStatement roleIdByName = connection.prepareStatement("SELECT role_id FROM role WHERE name = ?");
        roleIdByName.setString(1,roleName);
        ResultSet rs = roleIdByName.executeQuery();

        if (rs.next()){
            return rs.getLong(1);
        }
        return -1;
    }

    @Override
    public String findRoleNameById(long roleId) throws SQLException {
        PreparedStatement getRoleName = connection.prepareStatement("SELECT name FROM role WHERE role_id = ?");
        getRoleName.setLong(1,roleId);
        ResultSet rs = getRoleName.executeQuery();
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
    
    @Override
    public void create(Role role) throws SQLException {
        String sql = "INSERT INTO role (name) VALUES (?);";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1,role.getName());

        stmt.executeUpdate();

        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) {
            role.setRole_id(keys.getLong(1));
        }
    }

    @Override
    public void update(Role role) throws SQLException {
        String sql = "UPDATE role SET name = ? WHERE role_id = ?;";
        PreparedStatement update = connection.prepareStatement(sql);
        update.setString(1,role.getName());
        update.setLong(2,role.getRole_id());

        update.executeUpdate();
    }

    @Override
    public void remove(Role object) throws SQLException {
        String sql = "DELETE FROM role WHERE role_id = ?;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1,object.getRole_id());

        stmt.executeUpdate();
    }

    @Override
    public List<Role> findAll() throws SQLException{
        List<Role> roles = new ArrayList<>();
        Statement findAll = connection.createStatement();
        ResultSet rs = findAll.executeQuery("SELECT * FROM role");
        while (rs.next()){
            roles.add(new Role(
                    rs.getLong("role_id"),
                    rs.getString("name")
            ));
        }
        return roles;
    }
}
