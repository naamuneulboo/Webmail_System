<%-- 
    Document   : change_user_password.jsp
    Author     : jsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 비밀번호 변경 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            function getConfirmResult() {
                var result = confirm("사용자의 비밀번호를 변경 하시겠습니까?");
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
            <h2> 비밀번호를 수정할 사용자를 선택해 주세요. </h2> <br>

            <form name="ChangeUser" action="change_user_password.do" method="POST">
                <%
                    for (String userId : (java.util.List<String>) request.getAttribute("userList")) {
                        out.print("<label><input type=radio name=\"selectedUsers\" "
                            + "value=\"" + userId + "\" />");
                        out.println(userId + "</lable> <br>");
                    }
                %>
                <br>
                
                <label>새 비밀번호: <input type="password" name="newPassword" required></label><br><!-- comment -->

                <input type="submit" value="변경" name="change_command" 
                       onClick ="return getConfirmResult()"/>
                <input type="reset" value="초기화" />
            </form>
        </div>

        <%@include file="../footer.jspf" %>
    </body>
</html>
