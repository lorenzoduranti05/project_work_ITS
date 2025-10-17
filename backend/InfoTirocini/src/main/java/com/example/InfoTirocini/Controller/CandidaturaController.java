package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Model.Candidatura;
import com.example.InfoTirocini.Service.CandidaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidature")
@CrossOrigin(origins = "*")
public class CandidaturaController {
    
    @Autowired
    private CandidaturaService candidaturaService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> dettaglioCandidatura(@PathVariable Integer id) {
        try {
            Candidatura candidatura = candidaturaService.trovaPerID(id);
            return ResponseEntity.ok(candidatura);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/stato")
    public ResponseEntity<?> aggiornaStato(
            @PathVariable Integer id,
            @RequestParam String stato) {
        try {
            Candidatura aggiornata = candidaturaService.aggiornaStato(id, stato);
            return ResponseEntity.ok(aggiornata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminaCandidatura(@PathVariable Integer id) {
        try {
            candidaturaService.eliminaCandidatura(id);
            return ResponseEntity.ok("Candidatura eliminata con successo!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}