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
            function validatePassword() {
                var password = document.forms["AddUser"]["password"].value;
                var confirmPassword = document.forms["AddUser"]["password_check"].value;

                if (password != confirmPassword) {
                    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                    return false;
                }
                // 비밀번호 유효성 검사: 영문과 숫자를 조합한 6~14자리
                var passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,14}$/;
                if (!passwordPattern.test(password)) {
                    alert("비밀번호는 영문과 숫자를 조합한 6~14자리여야 합니다.");
                    return false;
                }
                return true;
            }

            function validateID() {
                var id = document.forms["AddUser"]["id"].value;

                // 정규 표현식을 사용하여 영어 또는 숫자가 아닌 문자가 있는지 확인
                var pattern = /^[a-zA-Z0-9]+$/;
                if (!pattern.test(id)) {
                    alert("ID는 영어나 숫자로만 이루어져야 합니다.");
                    return false;
                }
                // ID 유효성 검사: 영문과 숫자를 조합한 8~12자리
                var idPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/;
                if (!idPattern.test(id)) {
                    alert("ID는 영문과 숫자를 조합한 8~12자리여야 합니다.");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div id="main">
            <h2>사용자 회원가입</h2>
            <form name="AddUser" action="user_signup.do" method="POST" onsubmit="return validatePassword() && validateID();">
                <table border="0" align="left">
                    <tr>
                        <td>사용자 ID</td>
                        <td> <input type="text" name="id" value="" size="20" /> </td>
                    </tr>
                    <tr>
                        <td>비밀번호 </td>
                        <td> <input type="password" name="password" value="" /> </td>
                    </tr>
                    <tr>
                        <td>비밀번호 확인 </td>
                        <td> <input type="password" name="password_check" value="" /> </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="등록" name="register" />
                            <input type="reset" value="초기화" name="reset" />
                        </td>
                    </tr>
                </table>
            </form>


            <form name="CheckIDForm" action="check_id.do" method="POST">
                <tr>
                    <td colspan="2">
                        <input type="submit" value="확인" name="checkID" />
                    </td>
                </tr>
            </form>
        </div>
        <%@include file="footer.jspf"%>
    </body>

</html>