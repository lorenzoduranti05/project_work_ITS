package com.example.InfoTirocini.dto;

public class RispostaLoginDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String mail;
    private String ruolo;
    private String messaggio;
    

    public RispostaLoginDTO() {}
    
    public RispostaLoginDTO(Integer id, String nome, String cognome, 
                           String mail, String ruolo, String messaggio) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.mail = mail;
        this.ruolo = ruolo;
        this.messaggio = messaggio;
    }
    

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
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
    
    public String getRuolo() {
        return ruolo;
    }
    
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
    
    public String getMessaggio() {
        return messaggio;
    }
    
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}