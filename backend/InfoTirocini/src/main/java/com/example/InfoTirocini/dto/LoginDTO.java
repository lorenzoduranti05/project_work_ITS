package com.example.InfoTirocini.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    
    @Email(message = "Email non valida")
    @NotBlank(message = "Email obbligatoria")
    private String mail;
    
    @NotBlank(message = "Password obbligatoria")
    private String password;
}