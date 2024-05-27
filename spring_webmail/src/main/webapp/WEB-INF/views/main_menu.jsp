<%-- 
    Document   : main_menu
    Created on : 2022. 6. 10., 오후 3:15:45
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>

<!-- 제어기에서 처리하면 로직 관련 소스 코드 제거 가능!
<jsp:useBean id="pop3" scope="page" class="deu.cse.spring_webmail.model.Pop3Agent" />
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
%>
-->

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>주메뉴 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <style>
            .messages {
                margin-bottom: 20px;
            }
            .pagination-container {
                display: flex;
                justify-content: center;
                align-items: center;
                margin: 20px 0;
            }
            .pagination {
                display: flex;
                justify-content: center;
                list-style: none;
                padding: 0;
            }
            .pagination li {
                margin: 0 5px;
            }
            .pagination a {
                text-decoration: none;
                color: #007bff;
            }
            .pagination a:hover {
                text-decoration: underline;
            }
            .pagination .current-page {
                font-weight: bold;
            }
        </style>
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

        <!-- 메시지 삭제 링크를 누르면 바로 삭제되어 실수할 수 있음. 해결 방법은? -->

        <div id="main">
            <div class="messages">
                ${messageList}
            </div>

            <div class="pagination-container">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li><a href="?page=${currentPage - 1}&size=${size}"><<</a></li>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="page">
                        <li>
                            <a href="?page=${page}&size=${size}" class="${page == currentPage ? 'current-page' : ''}">${page}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li><a href="?page=${currentPage + 1}&size=${size}">>></a></li>
                        </c:if>
                </ul>
            </div>
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html> 