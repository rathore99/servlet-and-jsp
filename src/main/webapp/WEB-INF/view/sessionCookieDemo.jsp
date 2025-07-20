<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Session & Cookie Management Demo</title>
    <style>
        body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }
        .info { background: #f4f8fb; padding: 1em; border-radius: 8px; margin-bottom: 1em; }
        .logout { margin-top: 1em; }
    </style>
</head>
<body>
<h1>Session & Cookie Management Demo</h1>
<p>This page demonstrates real-world session and cookie management as used in enterprise web applications.</p>
<div class="info">
    <b>Request Counter (increments every 5 requests):</b> ${counter}
</div>
<c:if test="${not empty message}">
    <div class="info">${message}</div>
</c:if>
<c:choose>
    <c:when test="${empty username}">
        <form method="post" action="/session-cookie-demo">
            <label>Username:<br><input name="username" required style="width:100%"></label><br><br>
            <button type="submit" style="width:100%">Login</button>
        </form>
        <div class="info">
            <b>How it works:</b><br>
            On login, your username is stored in both the session and a cookie. If you revisit or refresh, the app checks the session first, then the cookie.
        </div>
    </c:when>
    <c:otherwise>
        <div class="info">
            <b>Welcome, ${username}!</b><br>
            <b>Session ID:</b> ${pageContext.session.id}<br>
            <b>Session Creation Time:</b> ${pageContext.session.creationTime}<br>
            <b>Session Last Accessed:</b> ${pageContext.session.lastAccessedTime}<br>
            <b>Cookie (user):</b> 
            <c:forEach var="cookie" items="${pageContext.request.cookies}">
                <c:if test="${cookie.name eq 'user'}">${cookie.value}</c:if>
            </c:forEach>
        </div>
        <form method="get" action="/session-cookie-demo" class="logout">
            <button type="submit" name="action" value="logout" style="width:100%">Logout</button>
        </form>
        <div class="info">
            <b>How it works:</b><br>
            Logging out invalidates the session and removes the cookie. This is a common pattern for secure user management in enterprise apps.
        </div>
    </c:otherwise>
</c:choose>
</body>
</html> 