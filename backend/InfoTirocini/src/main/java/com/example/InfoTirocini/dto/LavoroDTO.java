package com.example.InfoTirocini.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LavoroDTO {
    
    @NotBlank(message = "Nome lavoro obbligatorio")
    private String nome;
    
    @NotBlank(message = "Durata obbligatoria")
    private String durata;
    
    @NotBlank(message = "Orari obbligatori")
    private String orari;
    
    private String descrizione;
    
    private Integer aziendaId;
}