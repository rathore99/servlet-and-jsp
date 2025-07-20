<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Remove Profile Photo</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<h2>Remove Profile Photo</h2>
<form method="post" action="/employees-jsp/" style="margin-bottom:2em;">
    <input type="hidden" name="photoAction" value="remove">
    <label>Employee ID:<br><input name="id" required style="width:100%"></label><br><br>
    <button type="submit" style="width:100%">Remove Photo</button>
</form>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html> 