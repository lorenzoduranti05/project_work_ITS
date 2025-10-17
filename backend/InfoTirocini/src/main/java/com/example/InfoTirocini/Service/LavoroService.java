package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.*;
import com.example.InfoTirocini.Repository.*;
import com.example.InfoTirocini.dto.LavoroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LavoroService {
    
    @Autowired
    private LavoroRepository lavoroRepository;
    
    @Autowired
    private AziendaRepository aziendaRepository;
    
    @Autowired
    private CompetenzaRepository competenzaRepository;
    
    // Crea nuovo lavoro
    public Lavoro creaLavoro(LavoroDTO dto) {
        Lavoro lavoro = new Lavoro();
        lavoro.setNome(dto.getNome());
        lavoro.setDurata(dto.getDurata());
        lavoro.setOrari(dto.getOrari());
        lavoro.setDescrizione(dto.getDescrizione());
        
        if (dto.getAziendaId() != null) {
            Azienda azienda = aziendaRepository.findById(dto.getAziendaId())
                .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
            lavoro.setAzienda(azienda);
        }
        
        return lavoroRepository.save(lavoro);
    }
    
    // Ottieni tutti i lavori
    public List<Lavoro> trovaTuttiILavori() {
        return lavoroRepository.findAll();
    }
    
    // Ottieni lavoro per ID
    public Optional<Lavoro> trovaLavoroPerId(Integer id) {
        return lavoroRepository.findById(id);
    }
    
    // Aggiorna lavoro
    public Lavoro aggiornaLavoro(Integer id, LavoroDTO dto) {
        Lavoro lavoro = lavoroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato"));
        
        if (dto.getNome() != null) {
            lavoro.setNome(dto.getNome());
        }
        if (dto.getDurata() != null) {
            lavoro.setDurata(dto.getDurata());
        }
        if (dto.getOrari() != null) {
            lavoro.setOrari(dto.getOrari());
        }
        if (dto.getDescrizione() != null) {
            lavoro.setDescrizione(dto.getDescrizione());
        }
        if (dto.getAziendaId() != null) {
            Azienda azienda = aziendaRepository.findById(dto.getAziendaId())
                .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
            lavoro.setAzienda(azienda);
        }
        
        return lavoroRepository.save(lavoro);
    }
    
    // Elimina lavoro
    public void eliminaLavoro(Integer id) {
        if (!lavoroRepository.existsById(id)) {
            throw new RuntimeException("Lavoro non trovato");
        }
        lavoroRepository.deleteById(id);
    }
    
    // Cerca lavori per nome
    public List<Lavoro> cercaPerNome(String nome) {
        List<Lavoro> tutti = lavoroRepository.findAll();
        List<Lavoro> risultati = new ArrayList<>();
        
        for (Lavoro lavoro : tutti) {
            if (lavoro.getNome() != null && 
                lavoro.getNome().toLowerCase().contains(nome.toLowerCase())) {
                risultati.add(lavoro);
            }
        }
        
        return risultati;
    }
    
    // Trova lavori per azienda
    public List<Lavoro> trovaLavoriPerAzienda(Integer aziendaId) {
        List<Lavoro> tutti = lavoroRepository.findAll();
        List<Lavoro> risultati = new ArrayList<>();
        
        for (Lavoro lavoro : tutti) {
            if (lavoro.getAzienda() != null && 
                lavoro.getAzienda().getId().equals(aziendaId)) {
                risultati.add(lavoro);
            }
        }
        
        return risultati;
    }
    
    // Aggiungi competenze richieste al lavoro
    public Lavoro aggiungiCompetenze(Integer lavoroId, List<Integer> competenzeIds) {
        Lavoro lavoro = lavoroRepository.findById(lavoroId)
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato"));
        
        List<Competenza> competenze = new ArrayList<>();
        for (Integer compId : competenzeIds) {
            Competenza comp = competenzaRepository.findById(compId)
                .orElseThrow(() -> new RuntimeException("Competenza con ID " + compId + " non trovata"));
            competenze.add(comp);
        }
        
        lavoro.setCompetenzeRichieste(competenze);
        
        return lavoroRepository.save(lavoro);
    }
    
    // Rimuovi competenze dal lavoro
    public Lavoro rimuoviCompetenze(Integer lavoroId, List<Integer> competenzeIds) {
        Lavoro lavoro = lavoroRepository.findById(lavoroId)
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato"));
        
        List<Competenza> competenzeAttuali = lavoro.getCompetenzeRichieste();
        if (competenzeAttuali != null) {
            competenzeAttuali.removeIf(c -> competenzeIds.contains(c.getId()));
        }
        
        return lavoroRepository.save(lavoro);
    }
}