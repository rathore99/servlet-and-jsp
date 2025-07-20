<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Profile Photo Result</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<c:choose>
    <c:when test="${not empty invalidId}">
        <h2>Invalid Employee ID</h2>
    </c:when>
    <c:when test="${employee == null}">
        <h2>Employee Not Found</h2>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${photoAction eq 'add'}">
                <h2>Profile Photo Added</h2>
            </c:when>
            <c:when test="${photoAction eq 'update'}">
                <h2>Profile Photo Updated</h2>
            </c:when>
            <c:when test="${photoAction eq 'remove'}">
                <h2>Profile Photo Removed</h2>
            </c:when>
        </c:choose>
        <p>ID: ${employee.id}<br>
        Name: ${employee.name}<br>
        Department: ${employee.department}</p>
        <c:if test="${not empty employee.profilePhoto}">
            <img src='/${employee.profilePhoto}' alt='Profile Photo' style='max-width:120px;max-height:120px;border-radius:8px;border:1px solid #ccc;'>
        </c:if>
    </c:otherwise>
</c:choose>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html> 