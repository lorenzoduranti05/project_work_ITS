package com.example.InfoTirocini.dto;

import jakarta.validation.constraints.*;

public class RegistrazioneDTO {

    @NotBlank(message = "Nome obbligatorio")
    private String nome;
    
    @NotBlank(message = "Cognome obbligatorio")
    private String cognome;

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Formato email non valido")
    private String mail;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Conferma password obbligatoria")
    private String confermaPassword;


    public RegistrazioneDTO() {}


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

    public String getConfermaPassword() {
        return confermaPassword;
    }

    public void setConfermaPassword(String confermaPassword) {
        this.confermaPassword = confermaPassword;
    }
}