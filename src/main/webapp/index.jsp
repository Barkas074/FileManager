<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.nio.file.Path" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>File Manager</title>
        <style>
            a {
                text-decoration: none;
            }
            img {
                width: 16px;
            }
            .exit {
                position: absolute;
                right: 15px;
            }
        </style>
    </head>
    <body>
        <form class="exit" method="post">
            <input type="submit" name="exit" value="Выход"/>
        </form>
        <b>${date}</b>
        <h1>${currentPath}</h1>
        <hr>
        <img src="https://img.icons8.com/windows/344/up.png"/>
        <a href="?path=${currentPath.substring(0, currentPath.lastIndexOf("\\") + (currentPath.lastIndexOf("\\") != currentPath.indexOf("\\") ? 0 : 1))}">Вверх</a><br>
        <table>
            <tr>
                <th>Файл</th>
                <th>Размер</th>
                <th>Дата</th>
            </tr>
            <c:forEach var="directory" items="${directories}">
                <tr>
                    <td>
                    <img src="https://img.icons8.com/color/344/folder-invoices--v1.png"/>
                        <a href="?path=${directory.getAbsolutePath()}">${directory.getFileName()}</a>
                    </td>
                    <td>
                        <span>${directory.getSize()}</span>
                    </td>
                    <td>
                        <span>${directory.getDate()}</span>
                    </td>
                </tr>
            </c:forEach>
            <c:forEach var="file" items="${files}">
                <tr>
                    <td>
                    <img src="https://img.icons8.com/windows/344/file.png"/>
                        <a href="?path=${file.getAbsolutePath()}">${file.getFileName()}</a>
                    </td>
                    <td>
                        <span>${file.getSize()}</span>
                    </td>
                    <td>
                        <span>${file.getDate()}</span>
                    </td>
                    </tr>
            </c:forEach>
        </table>
    </body>
</html>