/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author jshpr
 */
@AllArgsConstructor
@Builder
public class inboxRow {
    @Getter
    private String from;
    @Getter
    private String to;
    @Getter
    private String subject;
    @Getter
    private String date;
    @Getter
    private String messageId;

    
}
