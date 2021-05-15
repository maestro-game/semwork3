<#ftl encoding='UTF-8'>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Спасибо за регистрацию</title>
    <style>
        h1 {
            color: cadetblue;
        }
        h2 {
            color: red;
        }
    </style>
</head>
<body>
<h1>Здравстуйте, ${user.name}. Благодарим за регистрацию на сервисе Charsender.</h1>
<h1>Ссылка для поддтверждения почты:</h1>
<h2><a href="${server_url}/confirm/${user.confirm}">тык</a></h2>
<img src="https://kpfu.ru//portal/docs/F_1313344075/02_logotip_3_stroki_cvetnoj.png">
</body>
</html>
