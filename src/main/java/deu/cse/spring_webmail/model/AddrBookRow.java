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
public class AddrBookRow {
    @Getter
    private String email;
    @Getter 
    private String name;
    @Getter 
    private String phone; 
    
}
