/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

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
            return matcher.group(1).trim();
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
                    String[] contentParts = part.split("\\r?\\n\\r?\\n", 2);
                    if (contentParts.length > 1) {
                        return contentParts[1].trim();
                    }
                }
            }
        }
        return "";
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
