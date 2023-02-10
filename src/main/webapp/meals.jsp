
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang = "ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
    <table>
        <caption>ЕДА</caption>
        <thead>
        <tr>
            <th>Дата</th>
            <th>Описание</th>
            <th>Калории</th>
            <th>Sosi</th>
            <th>Д5051</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="mealTo" items = "${mealList}">
            <tr style = "color: ${(mealTo.excess==true?'red':'lightseagreen')}">
                <td><c:out value="${mealTo.dateTime}" /> </td>
                <td><c:out value="${mealTo.description}" /></td>
                <td><c:out value="${mealTo.calories}" /></td>
                <td><a href="meals?action=update&id=<c:out value="${mealTo.id}"/>">Пропатчить и заапдейтить<a/></td>
                <td><a href="meals?action=delete&id=<c:out value="${mealTo.id}"/>">Удалить<a/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
<p><a href="meals?action=add">Add User</a></p>
</body>
</html>
