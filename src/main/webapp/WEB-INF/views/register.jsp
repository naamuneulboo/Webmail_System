<%-- 
    Document   : register
    Created on : 2024. 5. 2., 오후 7:48:24
    Author     : 이가연
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 회원가입 화면</title>
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
                var id = document.forms["UserSignUp"]["id"].value;
                var password = document.forms["UserSignUp"]["password"].value;
                var password_check = document.forms["UserSignUp"]["password_check"].value;

                // 공백 검사
                if (id.trim() === "" || password.trim() === "" || password_check.trim() === "") {
                    alert("모든 필드를 입력해주세요.");
                    return false;
                } 
                
                // 아이디 유효성 검사: 이메일 형식
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(id)) {
                    alert("올바른 이메일 주소를 입력해주세요.");
                    return false;
                }

                // 비밀번호 유효성 검사: 영어, 숫자, 특수문자로 이루어진 8~16자리
                var passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]{8,16}$/;
                if (!passwordRegex.test(password)) {
                    alert("비밀번호는 영어, 숫자, 특수문자로 이루어진 8~16자리여야 합니다.");
                    return false;
                }

                // 비밀번호 확인
                if (password !== password_check) {
                    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                    return false;
                }


                return true;
            }
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>
        <br>
        <div id="main">
            사용자 ID와 암호를 입력해 주시기 바랍니다. <br> <br>

            <form name="UserSignUp" action="user_sign_up.do" method="POST" onsubmit="return validateForm()">
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
                            <input type="submit" value="가입하기" name="register" />
                            <input type="reset" value="다시입력" name="reset" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>
