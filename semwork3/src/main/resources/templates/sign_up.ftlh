<#ftl encoding='UTF-8'>
<#import "spring.ftl" as spring/>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title><@spring.message 'sign_up.title'/></title>
</head>
<body>
<h1><@spring.message 'sign_up.title'/></h1>
<h2><a href="?lang=ru">РУ</a></h2>
<h2><a href="?lang=en">EN</a></h2>

<#if error?has_content>
    <div class="reg-container">
        <p>Error: ${error}</p>
    </div>
</#if>

<form action="/signUp" method="post">
<#--    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->

    <label for="id">Id</label>
    <input id="id" name="id" type="text" value="${(form.id)!}">

    <label for="password">Password</label>
    <input id="password" name="password" type="password">

    <label for="email">Email</label>
    <input id="email" name="email" type="email" value="${(form.email)!}">

    <label for="name">Name</label>
    <input id="name" name="name" type="text" value="${(form.name)!}">

    <label for="surname">Surname</label>
    <input id="surname" name="surname" type="text" value="${(form.surname)!}">

    <label for="phone">Phone</label>
    <input id="phone" name="phone" type="tel" value="${(form.phone)!}">

    <input value="Sign up" type="submit">
</form>
</body>
</html>