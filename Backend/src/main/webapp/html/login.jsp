<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>

<h1>Bejelentkezési felület DEMO</h1>
<form method="POST" action="${pageContext.request.contextPath}/login">
    <p><label for="email">Email: <input id="email" name="email"></label></p>
    <p>Jelszó: <input type="password" id="jelszo_id" name="password"></p>

    <input type="submit" value="Bejelentkezés"> <br>
</form>


<footer><a href="${pageContext.request.contextPath}/html/web.jsp"><input type="button" value="Vissza"></a></footer>
</body>
</html>