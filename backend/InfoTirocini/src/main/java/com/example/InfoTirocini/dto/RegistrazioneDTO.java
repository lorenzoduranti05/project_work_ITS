package com.example.InfoTirocini.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegistrazioneDTO {
    
    @NotBlank(message = "Nome obbligatorio")
    private String nome;
    
    @NotBlank(message = "Cognome obbligatorio")
    private String cognome;
    
    @Email(message = "Email non valida")
    @NotBlank(message = "Email obbligatoria")
    private String mail;
    
    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "Password minimo 6 caratteri")
    private String password;
}