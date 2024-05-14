<%-- 
    Document   : admin_menu.jsp
    Author     : jsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 관리 메뉴</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@ include file="../header.jspf" %>

        <div id="sidebar">
            <jsp:include page="sidebar_admin_menu.jsp" />
        </div>

        <div id="main">
            <h2> 메일 사용자 목록 </h2>
            <ul>
                <c:forEach var="user" items="${userList}">
                    <li>${user}</li>
                </c:forEach>
            </ul>
        </div>

        <%@include file="../footer.jspf" %>

        <script>
            <c:if test="${not empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </body>
</html>
