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
    </head>
    <script>
        function validatePassword() {
            var password = document.forms["AddUser"]["password"].value;
            var confirmPassword = document.forms["AddUser"]["password_check"].value;

            if (password != confirmPassword) {
                alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
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
            return true;
        }

        function checkID() {
            var id = document.getElementById("id").value;
            // AJAX를 사용하여 서버로 ID를 전송하고, 중복 여부를 확인합니다.
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "check_id.do", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var response = xhr.responseText;
                    if (response === "duplicate") {
                        alert("이미 사용 중인 ID입니다. 다른 ID를 선택해주세요.");
                    } else {
                        alert("사용 가능한 ID입니다.");
                    }
                }
            };
            xhr.send("id=" + id);
        }

    </script>
    <body>


        <div id="main">
            회원가입을 위한 정보를 입력해 주시기 바랍니다. <br> <br>

            <form name="AddUser" action="user_signup.do" method="POST" onsubmit="return validatePassword() && validateID();">


                <table border="0" align="left">
                    <tr>
                        <td>사용자 ID</td>
                        <td> <input type="text" name="id" value="" size="20" />  </td>
                    <button type="button" onclick="checkID()">ID 확인</button>
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
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>
