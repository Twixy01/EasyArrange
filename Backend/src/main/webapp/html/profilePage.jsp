<%@ page import="org.example.backend.Entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        tr{
            border: 1px solid black;
        }
        td{
            padding: 5px;
        }
    </style>
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    String role = (String) session.getAttribute("role");
%>
<h1>Felhasználói Profil</h1>
    <table style="">
        <image href="<%=user.getProfilePicture()%>"></image>
        <tr>
            <td>Név: </td>
            <td><%=user.getName()%></td>
        </tr>
        <tr>
            <td>Email: </td>
            <td><%=user.getEmail()%></td>
        </tr>
        <tr>
            <td>Role: </td>
            <td><%=role%></td>
        </tr>
    </table>



<footer>
    <a href="web.jsp"><input type="button" value="Vissza"></a>
    <a href="${pageContext.request.contextPath}/logout"><input type="button" value="Kijelentkezés"></a>
</footer>
</body>
</html>