 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>웹메일 시스템 메뉴</title>
</head>
<body>
<br> <br>

<span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>

<p> <a href="mail_tome"> 메일 읽기 </a> </p>
<p><a href="main_menu"> 이전 메뉴로 </a></p>
<p><a href="login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
</body>
</html>