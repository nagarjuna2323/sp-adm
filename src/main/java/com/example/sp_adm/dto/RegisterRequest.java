package com.example.sp_adm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName; 
    private Long managerId;  
    private String branch;    
    private String degree;     
    private String role;
}
