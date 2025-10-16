package com.example.InfoTirocini.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RispostaLoginDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String mail;
    private String ruolo;
    private String messaggio;
}