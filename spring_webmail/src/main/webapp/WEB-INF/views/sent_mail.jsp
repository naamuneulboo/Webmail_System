<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>보낸 메일함</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            <c:if test="${!empty msg}">
                alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>
        
        <div id="main">
            <table>
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>글쓴이</th>
                        <th>발신상대</th>
                        <th>제목</th>
                        <th>보낸날짜</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        String userid = (String) session.getAttribute("userid");
                        int rowNumber = 1;
                    %>
                    <c:forEach var="row" items="${dataRows}" varStatus="status">
                        <c:if test="${row.from == userid}">
                            <tr>
                                <td><%= rowNumber++ %></td>
                                <td>${row.from}</td>
                                <td>${row.to}</td>
                                 <td><a href="show_sentmessage?messageId=${row.messageId}">${row.subject}</a></td>
                                <td>${row.date}</td>
                                <td>
                                     <form action="${pageContext.request.contextPath}/deletesentMessage" method="post" style="display:inline;">
                                        <input type="hidden" name="messageId" value="${row.messageId}" />
                                        <input type="submit" value="삭제" />
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <%@include file="footer.jspf"%>
    </body>
</html>
