/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jshpr
 */
public class MessagesentParser {

    public static Map<String, String> parseMessage(String messageBody) {
        Map<String, String> messageDetails = new HashMap<>();
        messageDetails.put("from", extractField(messageBody, "From: "));
        messageDetails.put("to", removeDomain(extractField(messageBody, "To: ")));
        messageDetails.put("cc", extractField(messageBody, "Cc: "));
        messageDetails.put("date", removeTimezone(extractField(messageBody, "Date: ")));
        messageDetails.put("subject", extractField(messageBody, "Subject: "));
        messageDetails.put("content", extractContent(messageBody));
        return messageDetails;
    }

    private static String extractField(String messageBody, String fieldName) {
        String patternString = fieldName + "([^\\n]*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            String fieldValue = matcher.group(1).trim();
            if (fieldValue.matches("=\\?([^?]+)\\?B\\?([^?]+)\\?=")) {
                return decodeBase64(fieldValue);
            }
            return fieldValue;
        }
        return "";
    }

    private static String extractContent(String messageBody) {
        String boundaryPattern = "boundary=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(boundaryPattern);
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            String boundary = matcher.group(1);
            String[] parts = messageBody.split("--" + boundary);
            for (String part : parts) {
                if (part.contains("Content-Type: text/plain")) {
                    String charset = "UTF-8"; // Default charset
                    String contentTransferEncoding = "7bit"; // Default encoding

                    Matcher charsetMatcher = Pattern.compile("charset=([^;\\s]+)").matcher(part);
                    if (charsetMatcher.find()) {
                        charset = charsetMatcher.group(1).replace("\"", "");
                    }

                    Matcher encodingMatcher = Pattern.compile("Content-Transfer-Encoding: ([^\\s]+)").matcher(part);
                    if (encodingMatcher.find()) {
                        contentTransferEncoding = encodingMatcher.group(1);
                    }

                    String[] contentParts = part.split("\\r?\\n\\r?\\n", 2);
                    if (contentParts.length > 1) {
                        String content = contentParts[1].trim();
                        return decodeContent(content, charset, contentTransferEncoding);
                    }
                }
            }
        }
        return "";
    }

    private static String decodeContent(String content, String charset, String contentTransferEncoding) {
        try {
            byte[] decodedBytes;
            if ("base64".equalsIgnoreCase(contentTransferEncoding)) {
                decodedBytes = Base64.getDecoder().decode(content);
            } else {
                decodedBytes = content.getBytes(StandardCharsets.UTF_8); // Assuming 7bit or 8bit
            }
            return new String(decodedBytes, Charset.forName(charset));
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
    }

    private static String decodeBase64(String encoded) {
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
                byte[] decodedBytes = Base64.getDecoder().decode(encoded);
                return new String(decodedBytes, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return encoded;
        }
        return encoded;
    }

    private static String removeDomain(String email) {
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf("@"));
        }
        return email;
    }

    private static String removeTimezone(String date) {
        if (date != null && date.endsWith("+0900 (KST)")) {
            return date.substring(0, date.length() - 11).trim();
        }
        return date;
    }
}