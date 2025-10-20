package com.example.tirocini.dto;

import jakarta.validation.constraints.*;

public class CandidaturaDTO {
    
    @NotNull(message = "ID utente obbligatorio")
    private Integer utenteId;
    
    @NotNull(message = "ID lavoro obbligatorio")
    private Integer lavoroId;
    
    private String messaggio;
    

    public CandidaturaDTO() {}
    
    public CandidaturaDTO(Integer utenteId, Integer lavoroId, String messaggio) {
        this.utenteId = utenteId;
        this.lavoroId = lavoroId;
        this.messaggio = messaggio;
    }
    

    public Integer getUtenteId() {
        return utenteId;
    }
    
    public void setUtenteId(Integer utenteId) {
        this.utenteId = utenteId;
    }
    
    public Integer getLavoroId() {
        return lavoroId;
    }
    
    public void setLavoroId(Integer lavoroId) {
        this.lavoroId = lavoroId;
    }
    
    public String getMessaggio() {
        return messaggio;
    }
    
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}