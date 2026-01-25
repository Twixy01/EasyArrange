package org.example.backend.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.backend.Dao.interfaces.UserDao;
import org.example.backend.Dao.jdbc.UserDaoJdbc;
import org.example.backend.Entities.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user")!=null){
            response.sendRedirect("profile");
        }else{
            request.getRequestDispatcher("/html/login.jsp").forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        User user;
        Context initCtx = null;
        Connection conn = null;
        PrintWriter out = response.getWriter();
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource)envCtx.lookup("jdbc/easyarrange");
            conn = ds.getConnection();

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            UserDao<User> userDao = new UserDaoJdbc(conn);
            user = userDao.getUser(email,password);

            if (user == null) {
                out.println("Hibás felhasználó/jelszó");
            } else {
                String role = userDao.getRoleName(user.getRoleId());
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("role", role);
                response.sendRedirect("profile");
            }

            conn.close();

        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}