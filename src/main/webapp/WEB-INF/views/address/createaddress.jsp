<%--
    Document   : createaddress.jsp
    Author     : 82103
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, javax.naming.InitialContext, javax.naming.NamingException, javax.sql.DataSource" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('spring.datasource.driver-class-name')" var="db_driver"/>
<spring:eval expression="@environment.getProperty('spring.datasource.username')" var="db_username"/>
<spring:eval expression="@environment.getProperty('spring.datasource.password')" var="db_password"/>

<spring:eval expression="@configProperties['mysql.server.ip']" var="mysqlServerIp"/>
<spring:eval expression="@configProperties['mysql.server.port']" var="mysqlServerPort"/>
<html>
<head>
  <meta charset="UTF-8">
  <title>주소록 생성</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/main_style.css">
</head>
<body>
<h1>주소록 생성</h1>
<hr/>
<%
  final String JDBC_DRIVER = (String) pageContext.getAttribute("db_driver");

  final String mysqlServerIp = (String) request.getAttribute("mysql_server_ip");
  final String mysqlServerPort = (String) request.getAttribute("mysql_server_port");

  final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

  final String USER = (String) pageContext.getAttribute("db_username");
  final String PASSWORD = (String) pageContext.getAttribute("db_password");

  String name = request.getParameter("name");
  String email = request.getParameter("email");
  String phone = request.getParameter("phone");

  try {
    Class.forName(JDBC_DRIVER);
    Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

    // PreparedStatement 생성
    String sql = "INSERT INTO addrbook (name, email, phone) VALUES (?, ?, ?)";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, name);
    pstmt.setString(2, email);
    pstmt.setString(3, phone);

    int rowsAffected = pstmt.executeUpdate();

    if (rowsAffected > 0) {
%>
<!-- 작업이 성공했을 때 링크 추가 -->
<p><a href="<%= request.getContextPath() %>/showtable">주소록 테이블 보기</a></p>
<%
    } else {
      out.println("<p>주소록 생성에 실패했습니다.</p>");
    }

    pstmt.close();
    conn.close();

  } catch (Exception ex) {
    out.println("mysql_server = " + request.getParameter("mysql_server_ip"));
    out.println("오류가 발생했습니다. (발생오류 : " + ex.getMessage() + ")");
  }
%>
</body>
</html>
