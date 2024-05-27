<%-- 
    Document   : add_user.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 추가 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>

            function preventSpace(event) {
                if (event.keyCode === 32) {
                    event.preventDefault();
                    return false;
                }
                return true;
            }

            function validateForm() {
                var id = document.forms["AddUser"]["id"].value;
                var password = document.forms["AddUser"]["password"].value;
                var password_check = document.forms["AddUser"]["password_check"].value;

                // 공백 검사
                if (id.trim() === "" || password.trim() === "" || password_check.trim() === "") {
                    alert("모든 필드를 입력해주세요.");
                    return false;
                }

                // 아이디 유효성 검사: 영문자와 숫자만 허용, 4~10글자
                var idRegex = /^[a-zA-Z0-9]{4,10}$/;
                if (!idRegex.test(id)) {
                    alert("아이디는 영문자와 숫자만 허용하며, 4~10글자로 입력해주세요.");
                    return false;
                }


                // 비밀번호 확인
                if (password !== password_check) {
                    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                    return false;
                }

                // 비밀번호 유효성 검사: 영어, 숫자, 특수문자로 이루어진 8~16자리
                var passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]{8,16}$/;
                if (!passwordRegex.test(password)) {
                    alert("비밀번호는 영어, 숫자, 특수문자로 이루어진 8~16자리여야 합니다.");
                    return false;
                }




                return true;
            }
        </script> 
    </head>
    <body>
        <%@ include file="../header.jspf" %>

        <div id="sidebar">
            <jsp:include page="sidebar_admin_previous_menu.jsp" />
        </div>

        <div id="main">
            추가로 등록할 사용자 ID와 암호를 입력해 주시기 바랍니다. <br> <br>

            <form name="AddUser" action="add_user.do" method="POST" onsubmit="return validateForm()">
                <table border="0" align="left">
                    <tr>
                        <td>ID</td>
                        <td> <input type="text" name="id" value="" size="20" onkeydown="return preventSpace(event)" />  </td>
                    </tr>
                    <tr>
                        <td>비밀번호 </td>
                        <td> <input type="password" name="password" value="" onkeydown="return preventSpace(event)" /> </td>
                    </tr>
                    <tr>
                        <td>비밀번호 확인 </td>
                        <td> <input type="password" name="password_check" value="" onkeydown="return preventSpace(event)" /> </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="추가하기" name="register" />
                            <input type="reset" value="다시입력" name="reset" />
                        </td>
                    </tr>
                </table>
            </form> 
        </div>

        <%@include file="../footer.jspf" %>
    </body>
</html>