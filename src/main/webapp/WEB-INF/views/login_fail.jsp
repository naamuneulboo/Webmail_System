<%-- 
    Document   : login_fail
    Created on : 2024. 5. 27., 오후 11:21:56
    Author     : 이가연
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>




<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 실패</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <style>
            #login_fail_container {
                border: 2px solid black; /* 테두리 스타일 설정 */
                background-color: #ffffcc; /* 배경색 설정 */
                padding: 10px; /* 내부 여백 설정 */
                width: 1000px; /* 네모의 너비 설정 */
                height: 300px; /* 네모의 높이를 내용에 따라 자동으로 조절 */
                margin: 0 auto; /* 가운데 정렬 */
                text-align: center; /* 텍스트 가운데 정렬 */
                display: flex; /* Flexbox 사용 */
                justify-content: center; /* 수평 가운데 정렬 */
                align-items: center; /* 수직 가운데 정렬 */
            }
        </style>
        <script type="text/javascript">
            function gohome() {
                window.location = "${pageContext.request.contextPath}"
            }
        </script>
    </head>
    <body onload="setTimeout('gohome()', 5000)">

        <%@include file="header.jspf"%>
        <br>
        <br>
        <br>
        <div id="login_fail_container"> <!-- 네모 테두리로 감싸는 div 요소 -->
            
            <p id="login_fail">
                
                <br>
                <%= request.getParameter("userid")%>님, 로그인이 실패하였습니다.
                <br>
                올바른 사용자 ID와 암호를 사용하여 로그인하시기 바랍니다.
                <br>
                5초 뒤 자동으로 초기 화면으로 돌아갑니다.
                <br>
                자동으로 화면 전환이 일어나지 않을 경우
                <br>
                <!-- <a href="/WebMailSystem/" title="초기 화면">초기 화면</a>을 선택해 주세요.-->
                <a href="${pageContext.request.contextPath}" title="초기 화면">초기 화면</a>을 선택해 주세요.
            </p>
        </div>
            <br>
            <br>
        <%@include file="footer.jspf"%>

    </body>
</html>
