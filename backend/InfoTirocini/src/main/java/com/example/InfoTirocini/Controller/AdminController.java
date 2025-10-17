package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Model.Azienda;
import com.example.InfoTirocini.Model.Competenza;
import com.example.InfoTirocini.Service.AziendaService;
import com.example.InfoTirocini.Service.CompetenzaService;
import com.example.InfoTirocini.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private AziendaService aziendaService;
    
    @Autowired
    private CompetenzaService competenzaService;
    
    @Autowired
    private UtenteService utenteService;
    
    @GetMapping("/aziende")
    public ResponseEntity<List<Azienda>> tutteLeAziende() {
        List<Azienda> aziende = aziendaService.tutteLeAziende();
        return ResponseEntity.ok(aziende);
    }
    
    @PostMapping("/aziende")
    public ResponseEntity<?> creaAzienda(@RequestBody Map<String, String> body) {
        try {
            String nome = body.get("nome");
            Azienda nuova = aziendaService.creaAzienda(nome);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuova);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/aziende/{id}")
    public ResponseEntity<?> modificaAzienda(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        try {
            String nuovoNome = body.get("nome");
            Azienda aggiornata = aziendaService.aggiornaAzienda(id, nuovoNome);
            return ResponseEntity.ok(aggiornata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/aziende/{id}")
    public ResponseEntity<?> eliminaAzienda(@PathVariable Integer id) {
        try {
            aziendaService.eliminaAzienda(id);
            return ResponseEntity.ok("Azienda eliminata con successo!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/competenze")
    public ResponseEntity<List<Competenza>> tutteLeCompetenze() {
        List<Competenza> competenze = competenzaService.tutteLeCompetenze();
        return ResponseEntity.ok(competenze);
    }
    
    @PostMapping("/competenze")
    public ResponseEntity<?> creaCompetenza(@RequestBody Map<String, String> body) {
        try {
            String nome = body.get("nome");
            String tipo = body.get("tipoCompetenza");
            Competenza nuova = competenzaService.creaCompetenza(nome, tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuova);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/check/{userId}")
    public ResponseEntity<?> verificaAdmin(@PathVariable Integer userId) {
        try {
            boolean isAdmin = utenteService.isAdmin(userId);
            return ResponseEntity.ok(Map.of("isAdmin", isAdmin));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}