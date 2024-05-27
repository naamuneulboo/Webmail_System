<%-- 
    Document   : show_sentmessage
    Created on : 2024. 5. 21., 오후 8:21:36
    Author     : jshpr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메시지 내용</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_read_menu.jsp" />
        </div>

        <div>
            보낸사람: ${message.from}<br>
            받는사람: ${message.to}<br>
            CC &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: ${message.cc}<br>
            보낸날짜: ${message.date}<br>
            제목 &nbsp;&nbsp;&nbsp;&nbsp;: ${message.subject}<br>
            <hr>
            ${message.content}
            <%-- 첨부 파일 목록 출력 --%>
            <c:if test="${not empty message.attachments}">
                <br>
                <strong>첨부 파일:</strong><br>
                <c:forEach var="attachment" items="${message.attachments}">
                    <a href="download?userid=${userid}&filename=${attachment}" target="_blank">${attachment}</a><br>
                </c:forEach>
            </c:if>
        </div>


        <%@include file="../footer.jspf"%>
    </body>
</html>