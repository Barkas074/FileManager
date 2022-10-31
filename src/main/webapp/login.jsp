<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Вход</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        div {
            display: flex;
            flex-direction: column;
        }
        a {
            text-decoration: none;
            display: flex;
            border: 1px solid #767676;
            background: #EFEFEF;
            align-items: center;
            justify-content: center;
            color: black;
            border-radius: 2px;
        }
        a:Hover {
            background: #DBDBDB;
        }
        form {
            display: flex;
            width: 20vw;
            flex-wrap: wrap;
            flex-direction: column;
        }
        input {
            margin-bottom: 16px;
        }
        button {
            cursor: pointer;
        }
    </style>
</head>
<body>
    <div>
        <form method="post">
            <label>Логин:</label><input type="text" name="login">
            <label>Пароль:</label><input type="password" name="password">
            <button type="submit">Войти</button>
        </form>
        <a href="${registration}">Регистрация</a>
    </div>
</body>
</html>