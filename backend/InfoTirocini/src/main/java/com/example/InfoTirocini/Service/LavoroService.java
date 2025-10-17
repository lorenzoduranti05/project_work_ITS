package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Lavoro;
import com.example.InfoTirocini.Model.Azienda;
import com.example.InfoTirocini.Repository.LavoroRepository;
import com.example.InfoTirocini.Repository.AziendaRepository;
import com.example.InfoTirocini.dto.LavoroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LavoroService {
    
    @Autowired
    private LavoroRepository lavoroRepository;
    
    @Autowired
    private AziendaRepository aziendaRepository;
    
    // Ottieni tutti i lavori disponibili
    public List<Lavoro> tuttiILavori() {
        return lavoroRepository.findAll();
    }
    
    // Trova lavoro per ID
    public Lavoro trovaPerID(Integer id) {
        return lavoroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lavoro con ID " + id + " non trovato!"));
    }
    
    // Crea nuovo lavoro (solo admin)
    public Lavoro creaLavoro(LavoroDTO dto) {
        // Validazione base
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new RuntimeException("Il nome del lavoro è obbligatorio!");
        }
        
        // Crea nuovo lavoro
        Lavoro nuovoLavoro = new Lavoro();
        nuovoLavoro.setNome(dto.getNome());
        nuovoLavoro.setDurata(dto.getDurata());
        nuovoLavoro.setOrari(dto.getOrari());
        nuovoLavoro.setDescrizione(dto.getDescrizione());
        
        // Collega azienda se specificata
        if (dto.getAziendaId() != null) {
            Azienda azienda = aziendaRepository.findById(dto.getAziendaId())
                .orElseThrow(() -> new RuntimeException("Azienda con ID " + dto.getAziendaId() + " non trovata!"));
            nuovoLavoro.setAzienda(azienda);
        }
        
        // Salva e restituisci
        return lavoroRepository.save(nuovoLavoro);
    }
    
    // Aggiorna lavoro esistente (solo admin)
    public Lavoro aggiornaLavoro(Integer id, LavoroDTO dto) {
        // Trova lavoro esistente
        Lavoro lavoro = trovaPerID(id);
        
        // Aggiorna campi
        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            lavoro.setNome(dto.getNome());
        }
        
        if (dto.getDurata() != null && !dto.getDurata().isEmpty()) {
            lavoro.setDurata(dto.getDurata());
        }
        
        if (dto.getOrari() != null && !dto.getOrari().isEmpty()) {
            lavoro.setOrari(dto.getOrari());
        }
        
        if (dto.getDescrizione() != null) {
            lavoro.setDescrizione(dto.getDescrizione());
        }
        
        // Aggiorna azienda se specificata
        if (dto.getAziendaId() != null) {
            Azienda azienda = aziendaRepository.findById(dto.getAziendaId())
                .orElseThrow(() -> new RuntimeException("Azienda con ID " + dto.getAziendaId() + " non trovata!"));
            lavoro.setAzienda(azienda);
        }
        
        // Salva modifiche
        return lavoroRepository.save(lavoro);
    }
    
    // Elimina lavoro (solo admin)
    public void eliminaLavoro(Integer id) {
        // Verifica che esista
        if (!lavoroRepository.existsById(id)) {
            throw new RuntimeException("Lavoro con ID " + id + " non trovato!");
        }
        
        // Elimina
        lavoroRepository.deleteById(id);
    }
    
    // Conta totale lavori disponibili
    public long contaLavori() {
        return lavoroRepository.count();
    }
}