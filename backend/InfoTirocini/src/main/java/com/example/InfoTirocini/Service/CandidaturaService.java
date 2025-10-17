package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Candidatura;
import com.example.InfoTirocini.Model.Utente;
import com.example.InfoTirocini.Model.Lavoro;
import com.example.InfoTirocini.Repository.CandidaturaRepository;
import com.example.InfoTirocini.Repository.UtenteRepository;
import com.example.InfoTirocini.Repository.LavoroRepository;
import com.example.InfoTirocini.dto.CandidaturaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidaturaService {
    
    @Autowired
    private CandidaturaRepository candidaturaRepository;
    
    @Autowired
    private UtenteRepository utenteRepository;
    
    @Autowired
    private LavoroRepository lavoroRepository;
    
    // Crea nuova candidatura
    public Candidatura creaCandidatura(CandidaturaDTO dto) {
        // Verifica che l'utente non sia già candidato
        if (candidaturaRepository.existsByUtenteIdAndLavoroId(
                dto.getUtenteId(), dto.getLavoroId())) {
            throw new RuntimeException("Sei già candidato per questo lavoro!");
        }
        
        // Trova utente
        Utente utente = utenteRepository.findById(dto.getUtenteId())
            .orElseThrow(() -> new RuntimeException("Utente non trovato!"));
        
        // Trova lavoro
        Lavoro lavoro = lavoroRepository.findById(dto.getLavoroId())
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato!"));
        
        // Crea candidatura
        Candidatura candidatura = new Candidatura();
        candidatura.setUtente(utente);
        candidatura.setLavoro(lavoro);
        candidatura.setDataCandidatura(LocalDateTime.now());
        candidatura.setStato("IN_ATTESA");
        
        // Salva e restituisci
        return candidaturaRepository.save(candidatura);
    }
    
    // Ottieni candidature di un utente
    public List<Candidatura> candidatureUtente(Integer utenteId) {
        // Verifica che l'utente esista
        if (!utenteRepository.existsById(utenteId)) {
            throw new RuntimeException("Utente non trovato!");
        }
        
        return candidaturaRepository.findByUtenteId(utenteId);
    }
    
    // Ottieni candidature per un lavoro (per admin)
    public List<Candidatura> candidaturePerLavoro(Integer lavoroId) {
        // Verifica che il lavoro esista
        if (!lavoroRepository.existsById(lavoroId)) {
            throw new RuntimeException("Lavoro non trovato!");
        }
        
        return candidaturaRepository.findByLavoroId(lavoroId);
    }
    
    // Trova candidatura per ID
    public Candidatura trovaPerID(Integer id) {
        return candidaturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidatura non trovata!"));
    }
    
    // Aggiorna stato candidatura (per admin)
    public Candidatura aggiornaStato(Integer candidaturaId, String nuovoStato) {
        // Stati validi
        List<String> statiValidi = List.of("IN_ATTESA", "ACCETTATA", "RIFIUTATA");
        
        if (!statiValidi.contains(nuovoStato)) {
            throw new RuntimeException("Stato non valido! Usa: IN_ATTESA, ACCETTATA, RIFIUTATA");
        }
        
        // Trova candidatura
        Candidatura candidatura = trovaPerID(candidaturaId);
        
        // Aggiorna stato
        candidatura.setStato(nuovoStato);
        
        // Salva e restituisci
        return candidaturaRepository.save(candidatura);
    }
    
    // Elimina candidatura
    public void eliminaCandidatura(Integer id) {
        if (!candidaturaRepository.existsById(id)) {
            throw new RuntimeException("Candidatura non trovata!");
        }
        
        candidaturaRepository.deleteById(id);
    }
    
    // Conta candidature per un lavoro
    public long contaCandidaturePerLavoro(Integer lavoroId) {
        return candidaturaRepository.findByLavoroId(lavoroId).size();
    }
}