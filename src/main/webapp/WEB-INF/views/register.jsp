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
    
    <body>
        <%@include file="header.jspf"%>
        <br>
         <div id="main">
            사용자 ID와 암호를 입력해 주시기 바랍니다. <br> <br>

            <form name="UserSignUp" action="user_sign_up.do" method="POST">
                <table border="0" align="left">
                    <tr>
                        <td>사용자 ID</td>
                        <td> <input type="text" name="id" value="" size="20" />  </td>
                    </tr>
                    <tr>
                        <td>암호 </td>
                        <td> <input type="password" name="password" value="" /> </td>
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
