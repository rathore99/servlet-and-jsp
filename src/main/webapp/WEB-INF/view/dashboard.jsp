<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dashboard - Employee CRUD Demo</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<h2>Dashboard</h2>
<div style="background:#e7f3e7;padding:1em;border-radius:8px;margin-bottom:1em;">
    Welcome, <b>${username}</b>!
</div>
<form method="get" action="/auth">
    <button type="submit" name="action" value="logout" style="width:100%">Logout</button>
</form>
<div style="margin-top:2em;">
    <a href="/employees-jsp/">Go to Employee Management (JSP)</a>
</div>
</body>
</html> 