<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Employee Added</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<h2>Employee Added</h2>
<p>ID: ${employee.id}<br>
Name: ${employee.name}<br>
Department: ${employee.department}</p>
<c:if test="${not empty passwordMessage}">
    <div style="background:#e7f3e7;padding:1em;border-radius:8px;margin-bottom:1em;">${passwordMessage}</div>
</c:if>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html> 