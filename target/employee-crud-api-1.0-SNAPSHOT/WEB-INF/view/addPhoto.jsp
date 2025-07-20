<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Profile Photo</title>
    <style>body { max-width: 600px; margin: auto; font-family: Arial, sans-serif; }</style>
</head>
<body>
<h2>Add Profile Photo</h2>
<form method="post" action="/employees-jsp/" enctype="multipart/form-data" style="margin-bottom:2em;">
    <input type="hidden" name="photoAction" value="add">
    <label>Employee ID:<br><input name="id" required style="width:100%"></label><br><br>
    <label>Photo:<br><input type="file" name="photo" accept="image/*" required></label><br><br>
    <button type="submit" style="width:100%">Upload Photo</button>
</form>
<form method="get" action="/employees-jsp/"><button type="submit" style="width:100%">Back to Home</button></form>
</body>
</html> 