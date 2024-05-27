<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자 제거 화면</title>
    <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    <script>
        function getConfirmResult() {
            var result = confirm("사용자를 정말로 삭제하시겠습니까?");
            return result;
        }
    </script>
</head>
<body>
<%@ include file="../header.jspf" %>

<div id="sidebar">
    <jsp:include page="sidebar_admin_previous_menu.jsp" />
</div>

<div id="main">
    <h2> 삭제할 사용자를 선택해 주세요. </h2> <br>

    <form name="DeleteUser" action="delete_user.do" method="POST">
        <c:forEach var="userId" items="${userList}">
            <label>
                <input type="checkbox" name="selectedUsers" value="${userId}" />
                    ${userId}
            </label>
            <br>
        </c:forEach>
        <br>
        <input type="submit" value="제거" name="delete_command" onclick="return getConfirmResult()"/>
        <input type="reset" value="선택 전부 취소" />
    </form>
</div>

<%@include file="../footer.jspf" %>
</body>
</html>

