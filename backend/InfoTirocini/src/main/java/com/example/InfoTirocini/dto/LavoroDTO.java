package com.example.InfoTirocini.dto;

import jakarta.validation.constraints.*;

public class LavoroDTO {
    
    @NotBlank(message = "Nome lavoro obbligatorio")
    private String nome;
    
    private String durata;
    private String orari;
    private String descrizione;
    private Integer aziendaId;
    

    public LavoroDTO() {}
    
    public LavoroDTO(String nome, String durata, String orari, 
                     String descrizione, Integer aziendaId) {
        this.nome = nome;
        this.durata = durata;
        this.orari = orari;
        this.descrizione = descrizione;
        this.aziendaId = aziendaId;
    }
    

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDurata() {
        return durata;
    }
    
    public void setDurata(String durata) {
        this.durata = durata;
    }
    
    public String getOrari() {
        return orari;
    }
    
    public void setOrari(String orari) {
        this.orari = orari;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public Integer getAziendaId() {
        return aziendaId;
    }
    
    public void setAziendaId(Integer aziendaId) {
        this.aziendaId = aziendaId;
    }
}