package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbc extends JdbcConnection implements UserDao<User> {
    public UserDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public User findUser(String email, String password) throws SQLException {
        PreparedStatement getUser = connection.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?");
        getUser.setString(1, email);
        getUser.setString(2, password);
        ResultSet rs = getUser.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            );
        }
        return null;
    }

    @Override
    public List<User> findUsersByRoleName(String roleName) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement usersByRoleName = connection.prepareStatement("SELECT * FROM user JOIN role ON role.id = user.role_id WHERE role.name like ?");
        usersByRoleName.setString(1, roleName);
        ResultSet rs = usersByRoleName.executeQuery();

        while (rs.next()) {
            users.add(new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            ));
        }
        return users;
    }

    @Override
    public List<User> findAllStaff() throws SQLException {
        List<User> staffs = new ArrayList<>();
        Statement findAllStaff = connection.createStatement();
        ResultSet rs = findAllStaff.executeQuery("SELECT * FROM user JOIN role ON role.id = user.role_id WHERE role.name like 'staff'");

        while (rs.next()) {
            staffs.add(new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            ));
        }
        return staffs;
    }

    @Override
    public List<User> findAllCustomer() throws SQLException {
        List<User> customers = new ArrayList<>();
        Statement findAllCustomer = connection.createStatement();
        ResultSet rs = findAllCustomer.executeQuery("SELECT * FROM user JOIN role ON role.id = user.role_id WHERE role.name like 'customer'");

        while (rs.next()) {
            customers.add(new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            ));
        }
        return customers;
    }

    @Override
    public List<User> searchUsersByName(String namePart) throws SQLException {
        List<User> users = new ArrayList<>();
        Statement usersByName = connection.createStatement();
        ResultSet rs = usersByName.executeQuery("SELECT * FROM user WHERE name like ?");

        while (rs.next()) {
            users.add(new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            ));
        }
        return users;
    }

    @Override
    public User findUserById(long userId) throws SQLException {
        PreparedStatement findUserById = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        findUserById.setLong(1, userId);
        ResultSet rs = findUserById.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            );
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement findAll = connection.createStatement();
        ResultSet rs = findAll.executeQuery("SELECT * FROM user");
        while (rs.next()) {
            users.add(new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            ));

        }
        return users;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO user (name, email, profile_picture, password, role_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getProfilePicture());
            stmt.setString(4, user.getPassword());
            stmt.setLong(5, user.getRoleId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


