<%-- 
    Document   : update_addrbook
    Created on : 2024. 5. 1., 오후 5:04:59
    Author     : jshpr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>주소록 업데이트</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>      
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_address_menu.jsp" />
        </div>

        <div id="main">
            <h4>수정할 이메일을 적고 이름,전화번호를 수정하세요</h4>
            <form action="<%= request.getContextPath() %>/update.do" method="POST">
                <table border="0">
                    <tbody>
                        <tr>
                            <td>수정할 이메일</td>
                            <td><input type="text" name ="email" size="20" value="${addrbook.email}"/></td>
                        </tr>
                        <tr>
                            <td>이름</td>
                            <td><input type="text" name ="name" size="20" value="${addrbook.name}"/></td>
                        </tr>
                        <tr>
                            <td>전화번호</td>
                            <td><input type="text" name ="phone" size="20" value="${addrbook.phone}"/></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                    <center>
                        <input type="submit" value="업데이트" />&emsp;<input type="reset" value="초기화" />
                    </center>

                    </tbody>
                </table>

                <input type="hidden" name="id" value="${addrbook.id}"/> <!-- 업데이트할 주소록의 ID를 전송하기 위한 hidden 필드 -->
            </form>
        </div>
    </body>
</html>
