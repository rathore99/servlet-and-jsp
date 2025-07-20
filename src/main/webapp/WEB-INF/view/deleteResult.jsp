<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Employee Deleted</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<c:choose>
    <c:when test="${not empty invalidId}">
        <h2>Invalid ID</h2>
    </c:when>
    <c:when test="${!deleted}">
        <h2>Employee Not Found</h2>
    </c:when>
    <c:otherwise>
        <h2>Employee Deleted</h2>
        <p>Employee with ID ${deletedId} has been deleted.</p>
    </c:otherwise>
</c:choose>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html> 