<%-- 
    Document   : show_message.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 보기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_read_menu.jsp" />
            <p><a onclick="return confirm('정말로 삭제하시겠습니까?')" href="delete_mail.do?msgid=${msgid}"> 메일 삭제하기 </a></p>
        </div>

        <div id="msgBody">
            ${msg}
        </div>

        <%@include file="../footer.jspf"%>
    </body>
</html>