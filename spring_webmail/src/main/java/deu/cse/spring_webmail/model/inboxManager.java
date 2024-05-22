/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import static com.mysql.cj.conf.PropertyKey.PASSWORD;
import static jakarta.mail.Flags.Flag.USER;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.time.InstantSource.system;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author jshpr
 */
@Slf4j
public class inboxManager {

    @NonNull
    @Getter
    @Setter
    private String userid;
    @Getter
    @Setter
    private String toAddress;
    @Getter
    @Setter
    private String fromAddress;
    @Getter
    @Setter
    private String ccAddress;
    @Getter
    @Setter
    private String sentDate;
    @Getter
    @Setter
    private String subject;
    @Getter
    @Setter
    private String body;
    @Getter
    @Setter
    private String fileName;
    @Getter
    @Setter
    private String downloadTempDir = "C:/temp/download/";

    private String mysqlServerIp;
    private String mysqlServerPort;
    private String userName;
    private String password;
    private String jdbcDriver;

    public inboxManager() {
        log.debug("AddrBookManager(): mysqlServerIp = {} jdbcDriver = {}", mysqlServerIp, jdbcDriver);
    }

    public inboxManager(String mysqlServerIp, String mysqlServerPort, String userName, String password,
            String jdbcDriver) {
        this.mysqlServerIp = mysqlServerIp;
        this.mysqlServerPort = mysqlServerPort;
        this.userName = userName;
        this.password = password;
        this.jdbcDriver = jdbcDriver;
        log.debug("AddrBookManager(): mysqlServerIp = {} jdbcDriver = {}", mysqlServerIp, jdbcDriver);
    }

    public List<inboxRow> getAllRows() {
        List<inboxRow> dataList = new ArrayList<>();
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",
                mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL: {}", JDBC_URL);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);

            String sql = "SELECT message_body FROM inbox";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String messageBody = rs.getString("message_body");

                String from = extractField(messageBody, "From: ");
                String to = extractField(messageBody, "To: ");
                String subject = extractField(messageBody, "Subject: ");
                String date = extractField(messageBody, "Date: ");
                String messageId = extractField(messageBody, "Message-ID: ");

                if (date != null && date.endsWith("+0900 (KST)")) {
                    date = date.substring(0, date.length() - 11).trim(); // 끝에서 8글자 제거
                }

                if (to != null && to.contains("@")) {
                    to = to.substring(0, to.indexOf("@"));
                }

                dataList.add(new inboxRow(from, to, subject, date, messageId));
            }

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                log.error("자원 해제 중 오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            }
        }
        return dataList;
    }

    private String extractField(String messageBody, String fieldName) {
        String patternString = fieldName + "\\s*([^\\n]*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public boolean deleteMessageById(String messageId) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",
                mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL: {}", JDBC_URL);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);

            String deleteSql = "DELETE FROM inbox WHERE message_body LIKE ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, "%" + messageId + "%");

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                log.error("자원 해제 중 오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            }
        }
    }

    public String getMessageById(String messageId) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?serverTimezone=Asia/Seoul",
                mysqlServerIp, mysqlServerPort);

        log.debug("JDBC_URL: {}", JDBC_URL);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(JDBC_URL, this.userName, this.password);

            String sql = "SELECT message_body FROM inbox WHERE message_body LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + messageId + "%");

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String messageBody = rs.getString("message_body");
                return messageBody;
            }

        } catch (Exception ex) {
            log.error("오류가 발생했습니다. (발생오류: {})", ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                log.error("자원 해제 중 오류가 발생했습니다. (발생오류: {})", ex.getMessage());
            }
        }
        return null;
    }

}
