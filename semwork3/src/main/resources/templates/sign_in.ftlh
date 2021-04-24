<#ftl encoding='UTF-8'>
<#import "spring.ftl" as spring/>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title><@spring.message 'sign_in.title'/></title>
</head>
<body>
<h1><@spring.message 'sign_in.title'/></h1>
<#if error?has_content>
    <div class="reg-container">
        <p>Error: ${error}</p>
    </div>
</#if>
<#if info?has_content>
    <div class="reg-container">
        <p>Info: ${info}</p>
    </div>
</#if>
<form action="/signIn" method="post">
<#--    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
    <label for="email">Email</label>
    <input id="email" name="email" type="email" placeholder="Email">
    <label for="password">Password</label>
    <input id="password" name="password" type="password" placeholder="Password">
    <label for="remember-me">Remember me</label>
    <input id="remember-me" type="checkbox" name="remember-me">
    <input value="Sign in" type="submit">
</form>
</body>
</html>