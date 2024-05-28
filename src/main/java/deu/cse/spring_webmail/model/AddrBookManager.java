/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author jshpr
 */
@Slf4j
public class AddrBookManager {

    private String mysqlServerIp;
    private String mysqlServerPort;
    private String userName;
    private String password;
    private String jdbcDriver;

    public AddrBookManager() {
        log.debug("AddrBookManager(): mysqlServerIp = {} jdbcDriver = {}", mysqlServerIp, jdbcDriver);
    }

    public AddrBookManager(String mysqlServerIp, String mysqlServerPort, String userName, String password,
            String jdbcDriver) {
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.password = password;
        this.jdbcDriver = jdbcDriver;
        log.debug("AddrBookManager(): mysqlServerIp = {} jdbcDriver = {}", mysqlServerIp, jdbcDriver);
    }

    public List<AddrBookRow> getAllRows() {
        List<AddrBookRow> dataList = new ArrayList<>();
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",
                this.mysqlServerIp, this.mysqlServerPort);

        log.debug("JDBC_URL = {}, mysqlServerIp = {}, jdbcDriver = {}", JDBC_URL, mysqlServerIp, jdbcDriver);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, userName, password);

            stmt = conn.createStatement();

            String sql = "SELECT email,name,phone FROM addrbook";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                dataList.add(new AddrBookRow(email, name, phone));
            }
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            log.error("오류가 발생했습니다.1 (발생오류 :{})", ex.getMessage());
        }

        return dataList;
    }

    public void addRow(String email, String name, String phone) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",
                mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);

            String sql = "INSERT into addrbook VALUE(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);

            pstmt.executeUpdate();

            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            log.error("오류가 발생했습니다.2 (발생오류: {})", ex.getMessage());
        }
    }

    public void delRow(String email, String name, String phone) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",
                mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL", JDBC_URL);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);

            String sql = "DELETE FROM addrbook WHERE email=? AND name=? AND phone=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);

            pstmt.executeUpdate();

            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            log.error("오류가 발생했습니다.2 (발생오류: {})", ex.getMessage());
        }
    }

    public void updRow(String email, String name, String phone) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul", mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL: {}", JDBC_URL);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);

            // 주어진 이메일에 해당하는 행이 있는지 확인
            String selectSql = "SELECT * FROM addrbook WHERE email=?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // 이메일에 해당하는 행이 존재하는 경우
                // 이메일과 일치하는 행을 업데이트
                String updateSql = "UPDATE addrbook SET name=?, phone=? WHERE email=?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setString(3, email);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    log.debug("행이 업데이트되었습니다. 업데이트된 행 수: {}", rowsAffected);
                } else {
                    log.debug("행을 업데이트하지 못했습니다.");
                }
            } else { // 이메일에 해당하는 행이 존재하지 않는 경우
                log.debug("해당 이메일이 존재하지 않습니다."); // 수정: 이메일이 존재하지 않음을 로그에 출력합니다.
            }
        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("자원을 닫는 중 오류가 발생했습니다. (발생오류: {})", e.getMessage());
            }
        }
    }
}