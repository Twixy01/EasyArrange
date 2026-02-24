package org.example.backend.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.backend.Dao.RoleDao;
import org.example.backend.Dao.UserDao;
import org.example.backend.Dao.jpa.RoleDaoJPA;
import org.example.backend.Dao.jpa.UserDaoJPA;
import org.example.backend.Model.entity.User;
import org.example.backend.Service.RoleService;
import org.example.backend.Service.UserService;
import org.example.backend.Service.impl.RoleServiceImpl;
import org.example.backend.Service.impl.UserServiceImpl;
import org.hibernate.SessionFactory;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect("profile");
        } else {
            request.getRequestDispatcher("/html/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");

        UserDao userDao = new UserDaoJPA(sessionFactory);
        UserService userService = new UserServiceImpl(userDao);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = userService.getLoginUser(email,password);

        RoleDao roleDao = new RoleDaoJPA(sessionFactory);
        RoleService roleService = new RoleServiceImpl(roleDao);

        String role = roleService.getRoleById(user.getId()).getName();

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("role", role);
        response.sendRedirect("profile");
    }
}