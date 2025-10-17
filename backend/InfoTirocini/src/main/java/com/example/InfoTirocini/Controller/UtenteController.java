package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Model.Utente;
import com.example.InfoTirocini.Model.Candidatura;
import com.example.InfoTirocini.Service.UtenteService;
import com.example.InfoTirocini.Service.CandidaturaService;
import com.example.InfoTirocini.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "*")
public class UtenteController {
    
    @Autowired
    private UtenteService utenteService;
    
    @Autowired
    private CandidaturaService candidaturaService;
    
    @PostMapping("/registrazione")
    public ResponseEntity<?> registrazione(@Valid @RequestBody RegistrazioneDTO dto) {
        try {
            RispostaLoginDTO risposta = utenteService.registraUtente(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(risposta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        try {
            RispostaLoginDTO risposta = utenteService.login(dto);
            return ResponseEntity.ok(risposta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUtente(@PathVariable Integer id) {
        try {
            Utente utente = utenteService.trovaPerID(id);
            return ResponseEntity.ok(utente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/profilo")
    public ResponseEntity<?> aggiornaProfilo(
            @PathVariable Integer id,
            @RequestBody ProfiloDTO dto) {
        try {
            Utente aggiornato = utenteService.aggiornaProfilo(id, dto);
            return ResponseEntity.ok(aggiornato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}/candidature")
    public ResponseEntity<List<Candidatura>> leMieCandidature(@PathVariable Integer id) {
        try {
            List<Candidatura> candidature = candidaturaService.candidatureUtente(id);
            return ResponseEntity.ok(candidature);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Utente>> tuttiGliUtenti() {
        List<Utente> utenti = utenteService.tuttiGliUtenti();
        return ResponseEntity.ok(utenti);
    }
}