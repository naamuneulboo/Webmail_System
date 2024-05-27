<%-- 
    Document   : delete_addrbook
    Created on : 2024. 5. 1., 오후 5:04:59
    Author     : jshpr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>주소록 삭제 </title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>
        <h1>주소록 삭제</h1>
        <div id="sidebar">
            <jsp:include page="sidebar_address_menu.jsp" />
        </div>

        <form action="<%= request.getContextPath() %>/delete.do" method="POST">
            <table border="0">
                <tbody>
                    <tr>
                        <td>이름</td>
                        <td><input type="text" name ="name" size="20"/></td>
                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td><input type="text" name ="email" size="20"/></td>
                    </tr>
                    <tr>
                        <td>전화번호</td>
                        <td><input type="text" name ="phone" size="20"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                <center>
                    <input type="submit" value="삭제" />&emsp;<input type="reset" value="초기화" />
                </center>
                </tbody>

            </table>
        </form>
    </body>
</html>
