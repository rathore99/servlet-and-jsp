<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Result</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<c:choose>
    <c:when test="${not empty invalidId}">
        <h2>Invalid ID</h2>
    </c:when>
    <c:when test="${employee == null}">
        <h2>Employee Not Found</h2>
    </c:when>
    <c:otherwise>
        <h2>Employee Details</h2>
        <p>ID: ${employee.id}<br>
        Name: ${employee.name}<br>
        Department: ${employee.department}</p>
    </c:otherwise>
</c:choose>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html>

