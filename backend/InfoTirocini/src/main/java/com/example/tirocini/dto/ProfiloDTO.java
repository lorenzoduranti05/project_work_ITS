package com.example.tirocini.dto;

public class ProfiloDTO {
    private String nome;
    private String cognome;
    private String mail;
    private String password;
    private String confermaPassword;
    private String passwordAttuale;

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
}