package com.example.tirocini.dto;

import jakarta.validation.constraints.*;

public class LoginDTO {
    
    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Formato email non valido")
    private String mail;
    
    @NotBlank(message = "Password obbligatoria")
    private String password;
    

    public LoginDTO() {}
    
    public LoginDTO(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }
    
    public String getMail() {
        return mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}