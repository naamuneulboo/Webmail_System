<%-- 
    Document   : index
    Created on : 2024. 5. 27., 오후 11:21:56
    Author     : 이가연
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>


        <div id="login_form">
            <form method="POST" action="login.do?menu=<%= CommandType.LOGIN %>">
                사용자: <input type="text" name="userid" size="20" autofocus> <br />
                암&nbsp;&nbsp;&nbsp;호: <input type="password" name="passwd" size="20"> <br /> <br />
                <input type="submit" value="로그인" name="B1">&nbsp;&nbsp;&nbsp;
                <input type="reset" value="다시 입력" name="B2"> 
            </form>

        </div>

        <div id="register_button">
            <form method="GET" action="register">
                <input type="submit" value="회원가입">
            </form>
            &nbsp;&nbsp;⇑ ⇑ 웹 메일 시스템 회원가입 바로가기
        </div>

        <div style="color:blue">
            <c:if test="${not empty msg}">
                <p>${msg}</p>
            </c:if>
        </div>   
   
        <%@include file="footer.jspf"%>
    </body>

</html>
