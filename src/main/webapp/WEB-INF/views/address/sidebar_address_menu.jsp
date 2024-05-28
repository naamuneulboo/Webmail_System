<%-- 
    Document   : sidebar_menu
    Created on : 2024. 5. 27., 오후 11:21:56
    Author     : 이가연
--%>

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

        <p> <a href="createadd"> 주소록 추가 </a> </p>
        <p> <a href="delete_addrbook"> 주소록 삭제 </a> </p>
        <p> <a href="update_addrbook"> 주소록 업데이트 </a> </p>
        <p> <a href="showtable"> 주소록 화면으로 </a> </p>       
        <a href="main_menu"> 이전 메뉴로 </a>
    </body>
</html>