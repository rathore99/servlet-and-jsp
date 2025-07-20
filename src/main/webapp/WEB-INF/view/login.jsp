<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login - Employee CRUD Demo</title>
    <style>body { max-width: 400px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<h2>Login</h2>
<c:if test="${not empty message}">
    <div style="background:#f8d7da;padding:1em;border-radius:8px;margin-bottom:1em;">${message}</div>
</c:if>
<form method="post" action="/auth">
    <label>Username:<br><input name="username" required style="width:100%"></label><br><br>
    <label>Password:<br><input type="password" name="password" required style="width:100%"></label><br><br>
    <button type="submit" style="width:100%">Login</button>
</form>
<div style="margin-top:2em;font-size:0.9em;color:#888;">Demo user: <b>admin</b> / <b>admin123</b></div>
</body>
</html> 