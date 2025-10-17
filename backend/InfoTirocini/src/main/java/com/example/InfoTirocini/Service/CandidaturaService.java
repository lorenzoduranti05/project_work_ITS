package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.*;
import com.example.InfoTirocini.Repository.*;
import com.example.InfoTirocini.dto.CandidaturaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        // Verifica che non esista già una candidatura
        List<Candidatura> esistenti = candidaturaRepository.findAll();
        for (Candidatura c : esistenti) {
            if (c.getUtente().getId().equals(dto.getUtenteId()) && 
                c.getLavoro().getId().equals(dto.getLavoroId())) {
                throw new RuntimeException("Candidatura già esistente per questo lavoro");
            }
        }
        
        Utente utente = utenteRepository.findById(dto.getUtenteId())
            .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        Lavoro lavoro = lavoroRepository.findById(dto.getLavoroId())
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato"));
        
        Candidatura candidatura = new Candidatura();
        candidatura.setUtente(utente);
        candidatura.setLavoro(lavoro);
        candidatura.setDataCandidatura(LocalDateTime.now());
        candidatura.setStato("In attesa");
        
        return candidaturaRepository.save(candidatura);
    }
    
    // Ottieni tutte le candidature
    public List<Candidatura> trovaTutteLeCandidature() {
        return candidaturaRepository.findAll();
    }
    
    // Trova candidature per utente
    public List<Candidatura> trovaCandidaturePerUtente(Integer utenteId) {
        List<Candidatura> tutte = candidaturaRepository.findAll();
        List<Candidatura> risultati = new ArrayList<>();
        
        for (Candidatura c : tutte) {
            if (c.getUtente().getId().equals(utenteId)) {
                risultati.add(c);
            }
        }
        
        return risultati;
    }
    
    // Trova candidature per lavoro
    public List<Candidatura> trovaCandidaturePerLavoro(Integer lavoroId) {
        List<Candidatura> tutte = candidaturaRepository.findAll();
        List<Candidatura> risultati = new ArrayList<>();
        
        for (Candidatura c : tutte) {
            if (c.getLavoro().getId().equals(lavoroId)) {
                risultati.add(c);
            }
        }
        
        return risultati;
    }
    
    // Aggiorna stato candidatura
    public Candidatura aggiornaStatoCandidatura(Integer id, String nuovoStato) {
        Candidatura candidatura = candidaturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidatura non trovata"));
        
        if (!nuovoStato.equals("In attesa") && 
            !nuovoStato.equals("Accettata") && 
            !nuovoStato.equals("Rifiutata")) {
            throw new RuntimeException("Stato non valido");
        }
        
        candidatura.setStato(nuovoStato);
        return candidaturaRepository.save(candidatura);
    }
    
    // Elimina candidatura
    public void eliminaCandidatura(Integer id) {
        if (!candidaturaRepository.existsById(id)) {
            throw new RuntimeException("Candidatura non trovata");
        }
        candidaturaRepository.deleteById(id);
    }
    
    // Ritira candidatura
    public void ritiraCandidatura(Integer utenteId, Integer candidaturaId) {
        Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
            .orElseThrow(() -> new RuntimeException("Candidatura non trovata"));
        
        if (!candidatura.getUtente().getId().equals(utenteId)) {
            throw new RuntimeException("Non autorizzato a ritirare questa candidatura");
        }
        
        candidaturaRepository.deleteById(candidaturaId);
    }
    
    // Conta candidature per lavoro
    public long contaCandidaturePerLavoro(Integer lavoroId) {
        List<Candidatura> tutte = candidaturaRepository.findAll();
        long count = 0;
        
        for (Candidatura c : tutte) {
            if (c.getLavoro().getId().equals(lavoroId)) {
                count++;
            }
        }
        
        return count;
    }
    
    // Trova candidature per stato
    public List<Candidatura> trovaCandidaturePerStato(String stato) {
        List<Candidatura> tutte = candidaturaRepository.findAll();
        List<Candidatura> risultati = new ArrayList<>();
        
        for (Candidatura c : tutte) {
            if (c.getStato() != null && c.getStato().equals(stato)) {
                risultati.add(c);
            }
        }
        
        return risultati;
    }
    
    // Verifica se utente si è già candidato
    public boolean isUtenteCandidato(Integer utenteId, Integer lavoroId) {
        List<Candidatura> tutte = candidaturaRepository.findAll();
        
        for (Candidatura c : tutte) {
            if (c.getUtente().getId().equals(utenteId) && 
                c.getLavoro().getId().equals(lavoroId)) {
                return true;
            }
        }
        
        return false;
    }
}