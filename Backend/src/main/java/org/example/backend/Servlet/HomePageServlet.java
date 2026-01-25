package org.example.backend.Servlet;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "homePageServlet", value = "/homepage")
public class HomePageServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("This is the homepage.");
        response.sendRedirect("html/web.jsp");
    }
    public void destroy() {
    }
}