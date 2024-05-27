<%-- 
    Document   : alladdress
    Created on : 2024. 5. 27., 오후 11:21:56
    Author     : 이가연
--%>

<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('spring.datasource.driver-class-name')" var="db_driver" />
<spring:eval expression="@environment.getProperty('spring.datasource.username')" var="db_username" />
<spring:eval expression="@environment.getProperty('spring.datasource.password')" var="db_password" />

<spring:eval expression="@configProperties['mysql.server.ip']" var="mysqlServerIp"/>
<spring:eval expression="@configProperties['mysql.server.port']" var="mysqlServerPort"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <title>주소록 전체보기</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main_style.css">
</head>
<body>
    <h1>주소록</h1>
    <hr/>
    <%
        final String JDBC_DRIVER = (String) pageContext.getAttribute("db_driver");

        final String mysqlServerIp = (String) request.getAttribute("mysql_server_ip");
        final String mysqlServerPort = (String) request.getAttribute("mysql_server_port");

        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",mysqlServerIp,mysqlServerPort);

        final String USER = (String) pageContext.getAttribute("db_username");
        final String PASSWORD = (String) pageContext.getAttribute("db_password");

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "SELECT email, name, phone FROM addrbook";
            ResultSet rs = stmt.executeQuery(sql);
    %>
    <table border="1">
        <thead>
            <tr>
                <th>이름</th>
                <th>이메일</th>
                <th>전화번호</th>
            </tr>
        </thead>
        <tbody>
        <%
            while (rs.next()){
                out.println("<tr>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("phone") + "</td>");
                out.println("</tr>");
            }
            rs.close();
            stmt.close();
            conn.close();

        %>
        </tbody>
    </table>
    <%
        }catch (Exception ex){
            out.println("mysql_server = " + request.getParameter("mysql_server_ip"));
            out.println("오류가 발생했습니다. (발생오류 : " + ex.getMessage() + ")");
        }
    %>
</body>
</html>