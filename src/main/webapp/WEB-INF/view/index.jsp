<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Employees</title>
    <style>
        body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }
        .actions { display: flex; flex-direction: column; gap: 1em; margin: 2em 0; }
        button { width: 100%; padding: 0.5em; font-size: 1em; }
        ul { padding-left: 1.2em; }
    </style>
</head>
<body>
<h1>Employee Management (JSP)</h1>
<div class="actions">
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="add">Add Employee</button></form>
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="update">Update Employee</button></form>
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="delete">Delete Employee</button></form>
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="search">Search Employee</button></form>
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="addPhoto">Add Profile Photo</button></form>
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="updatePhoto">Update Profile Photo</button></form>
    <form method="get" action="/employees-jsp/"><button type="submit" name="action" value="removePhoto">Remove Profile Photo</button></form>
</div>
<div style="margin-top:2em;">
    <form method="get" action="/dashboard">
        <button type="submit" style="width:100%">Go to Dashboard</button>
    </form>
</div>
<hr>
<h2>All Employees</h2>
<ul>
    <c:forEach var="emp" items="${employees}">
        <li>${emp.id}: ${emp.name} (${emp.department})
            <c:if test="${not empty emp.profilePhoto}">
                <br><img src='/${emp.profilePhoto}' alt='Profile Photo' style='max-width:80px;max-height:80px;border-radius:8px;border:1px solid #ccc;'>
            </c:if>
        </li>
    </c:forEach>
</ul>
</body>
</html> 