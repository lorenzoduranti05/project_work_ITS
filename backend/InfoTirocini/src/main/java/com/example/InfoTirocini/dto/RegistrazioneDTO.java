package com.example.InfoTirocini.dto;

import jakarta.validation.constraints.*;

public class RegistrazioneDTO {
    
    @NotBlank(message = "Nome obbligatorio")
    @Size(min = 2, max = 50, message = "Nome deve essere tra 2 e 50 caratteri")
    private String nome;
    
    @NotBlank(message = "Cognome obbligatorio")
    @Size(min = 2, max = 50, message = "Cognome deve essere tra 2 e 50 caratteri")
    private String cognome;
    
    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Formato email non valido")
    private String mail;
    
    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "Password deve avere almeno 6 caratteri")
    private String password;
    
    // Costruttori
    public RegistrazioneDTO() {}
    
    public RegistrazioneDTO(String nome, String cognome, String mail, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.mail = mail;
        this.password = password;
    }
    
    // Getter e Setter
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
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