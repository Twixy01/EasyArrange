package org.example.backend.Dao.jdbc;

import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Entities.User;

import javax.naming.Name;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoJdbc extends JdbcConnection implements UserDao<User> {
    public UserDaoJdbc(Connection connection) {
        super(connection);
    }

    @Override
    public User getUser(String email, String password) throws SQLException {
        PreparedStatement getUser = connection.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?");
        getUser.setString(1,email);
        getUser.setString(2,password);
        ResultSet rs = getUser.executeQuery();

        if (rs.next()){
            return new User(
                    rs.getString("Name"),
                    rs.getString("email"),
                    rs.getString("profile_picture"),
                    rs.getString("password"),
                    rs.getLong("role_id")
            );
        }
        return null;
    }
}
