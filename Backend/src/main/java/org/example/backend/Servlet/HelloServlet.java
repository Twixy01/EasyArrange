package org.example.backend.Servlet;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Dao.jdbc.UserDaoJdbc;
import org.example.backend.Entities.User;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        Properties properties = new Properties();
        try (InputStream inputStream = Thread.currentThread().
                getContextClassLoader().
                getResourceAsStream("db_connection.properties")) {

            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String DB_CONN = properties.getProperty("db_connection");
        final String DB_USER = properties.getProperty("db_user");
        final String DB_PASSWORD = properties.getProperty("db_password");
        String driver = properties.getProperty("driver");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        User user;
        try (Connection connection = DriverManager.getConnection(DB_CONN, DB_USER, DB_PASSWORD)){
            UserDao<User> userDao = new UserDaoJdbc(connection);
            user = userDao.getUser("test@gmail.com","1234");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out = response.getWriter();
        if (user != null){
            out.println("<html><body>");
            out.println("<h1>Profile Page</h1>");
            out.println("<p>name: </p>" + "<p>" + user.getName() + "</p><br>");
            out.println("<p>email: </p>" + "<p>" + user.getEmail() + "</p><br>");
            out.println("<p>profile: </p>" + "<p>" + user.getProfilePicture() + "</p><br>");
            out.println("<p>password: </p>" + "<p>" + user.getPassword() + "</p><br>");
            out.println("<p>Role: </p>" + "<p>" + user.getRoleId() + "</p><br>");
            out.println("</body></html>");
        }
        else{
            out.println("<html><body>");

        }

    }

    public void destroy() {
    }
}