package com.example.tirocini.dto;

import jakarta.validation.constraints.*;

public class ProfiloDTO {

    @NotBlank(message = "Nome obbligatorio")
    @Pattern(regexp = "^[\\p{L} ']+$", message = "Il nome può contenere solo lettere, spazi e apostrofi")
    private String nome;

    @NotBlank(message = "Cognome obbligatorio")
    @Pattern(regexp = "^[\\p{L} ']+$", message = "Il cognome può contenere solo lettere, spazi e apostrofi")
    private String cognome;

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Formato email non valido")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Formato email non valido (es. utente@dominio.com)")
    private String mail;

    private String password;

    private String confermaPassword;

    @NotBlank(message = "Password attuale obbligatoria per salvare le modifiche")
    private String passwordAttuale;

    private String imageDataUrl;

    public ProfiloDTO() {}

    public ProfiloDTO(String nome, String cognome, String mail, String password, String confermaPassword, String passwordAttuale) {
        this.nome = nome;
        this.cognome = cognome;
        this.mail = mail;
        this.password = password;
        this.confermaPassword = confermaPassword;
        this.passwordAttuale = passwordAttuale;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfermaPassword() { return confermaPassword; }
    public void setConfermaPassword(String confermaPassword) { this.confermaPassword = confermaPassword; }

    public String getPasswordAttuale() { return passwordAttuale; }
    public void setPasswordAttuale(String passwordAttuale) { this.passwordAttuale = passwordAttuale; }

    public String getImageDataUrl() { return imageDataUrl; }
    public void setImageDataUrl(String imageDataUrl) { this.imageDataUrl = imageDataUrl; }
}