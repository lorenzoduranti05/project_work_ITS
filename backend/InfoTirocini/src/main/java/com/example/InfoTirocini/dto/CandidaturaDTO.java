package com.example.InfoTirocini.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CandidaturaDTO {
    
    @NotNull(message = "ID utente obbligatorio")
    private Integer utenteId;
    
    @NotNull(message = "ID lavoro obbligatorio")
    private Integer lavoroId;
}