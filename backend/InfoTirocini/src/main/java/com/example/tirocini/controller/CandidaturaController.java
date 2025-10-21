package com.example.tirocini.controller;

import com.example.tirocini.model.Utente;
import com.example.tirocini.service.CandidaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidature") 
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @PostMapping
    public ResponseEntity<?> applica(
            @RequestParam("lavoroId") Integer lavoroId,
            @AuthenticationPrincipal Utente utente) { 
        
        if (utente == null) {
            return ResponseEntity.status(401).body("Utente non autenticato.");
        }

        try {
            candidaturaService.creaCandidatura(lavoroId, utente);
            return ResponseEntity.ok().body("Candidatura inviata con successo.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}