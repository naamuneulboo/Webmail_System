/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author jshpr
 */
@Slf4j
public class inboxManager {

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
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul",
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
        String patternString = fieldName + "([^\\n]*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            String fieldValue = matcher.group(1).trim();
            // Check if the field value is Base64 encoded
            if (fieldValue.matches("=\\?([^?]+)\\?B\\?([^?]+)\\?=")) {
                return decodeBase64(fieldValue);
            }
            return fieldValue;
        }
        return "";
    }

    public boolean deleteMessageById(String messageId) {
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul",
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
        final String JDBC_URL = String.format("jdbc:mysql://%s:%s/webmail?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul",
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

    private String decodeBase64(String encoded) {
        try {
            if (encoded.matches("=\\?([^?]+)\\?B\\?([^?]+)\\?=")) {
                Pattern pattern = Pattern.compile("=\\?([^?]+)\\?B\\?([^?]+)\\?=");
                Matcher matcher = pattern.matcher(encoded);
                if (matcher.find()) {
                    String charset = matcher.group(1);
                    String base64Content = matcher.group(2);
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
                    return new String(decodedBytes, Charset.forName(charset));
                }
            } else {
                // Assume the string is directly Base64 encoded without any charset information
                byte[] decodedBytes = Base64.getDecoder().decode(encoded);
                return new String(decodedBytes, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return encoded;
        }
        return encoded;
    }

}
