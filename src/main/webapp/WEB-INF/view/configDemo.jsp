<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>ServletConfig & ServletContext Demo</title>
    <style>
        body { max-width: 700px; margin: auto; font-family: sans-serif; }
        ul { padding-left: 1.2em; }
        h1, h2 { color: #2a4d7a; }
    </style>
</head>
<body>
<h1>ServletConfig & ServletContext Demo</h1>
<h2>init() Example</h2>
<p>This servlet reads config params at startup using <b>init()</b> and demonstrates <b>ServletConfig</b> and <b>ServletContext</b> usage, as done in enterprise apps for configuration and resource sharing.</p>
<ul>
    <li><b>Servlet-specific setting (from web.xml):</b> ${servletSpecificSetting}</li>
    <li><b>App-wide setting (from web.xml):</b> ${appWideSetting}</li>
    <li><b>Startup time (context attribute):</b> ${startupTime}</li>
</ul>
<h2>ServletContext Example</h2>
<p>Context path: <b>${contextPath}</b></p>
<p>Real path to /images: <b>${imagesRealPath}</b></p>
<h2>ServletConfig Example</h2>
<p>Servlet name: <b>${servletName}</b></p>
<p>All servlet init params:</p>
<ul>
    <c:forEach var="name" items="${initParams}">
        <li>${name}: ${servletConfig.getInitParameter(name)}</li>
    </c:forEach>
</ul>
<h2>destroy() Example</h2>
<p>When the servlet is unloaded (e.g., app shutdown), <b>destroy()</b> is called. See logs for cleanup actions.</p>
</body>
</html> 