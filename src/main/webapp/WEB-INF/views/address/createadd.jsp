<%-- 
    Document   : createadd
    Created on : 2024. 5. 27., 오후 11:22:27
    Author     : 이가연
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>주소록 입력</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/main_style.css">
</head>
<body>
<h1>주소록 입력</h1>
<form method="post" action="<%= request.getContextPath() %>/createadd">
  <table border="0" align="left">
    <tr>
      <td><label for="name">이름:</label></td>
      <td><input type="text" id="name" name="name" required></td>
    </tr>
    <tr>
      <td><label for="email">이메일:</label></td>
      <td>
        <input type="text" id="email" name="email" placeholder="@localhost의 앞부분만 입력해주세요" required>
      </td>
    </tr>
    <tr>
      <td><label for="phone">전화번호:</label></td>
      <td><input type="text" id="phone" name="phone" required></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="주소록 추가">
        <input type="reset" value="초기화">
      </td>
    </tr>
  </table>
</form>
</body>
</html>