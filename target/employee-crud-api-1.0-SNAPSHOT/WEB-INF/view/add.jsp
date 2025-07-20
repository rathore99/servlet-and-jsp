<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Employee</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<h2>Add Employee</h2>
<form method="post" action="/employees-jsp/" style="margin-bottom:2em;">
    <label>Name:<br><input name="name" required style="width:100%"></label><br><br>
    <label>Department:<br><input name="department" required style="width:100%"></label><br><br>
    <button type="submit" style="width:100%">Add Employee</button>
</form>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html> 