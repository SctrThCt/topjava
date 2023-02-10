
<%@ page contentType="text/html;charset=UTF-8"%>
<html lang = "ru">
<head>
    <title>Save meal form</title>
</head>
<body>
<form method="post">
    <input type="datetime-local" name="dateTime">Date<br>
    <input type="text" name="description">Description<br>
    <input type="number" name="calories">Calories<br>
    <input type="submit" value="Submit">
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>
