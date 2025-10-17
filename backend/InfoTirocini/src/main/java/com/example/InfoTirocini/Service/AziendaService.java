package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Azienda;
import com.example.InfoTirocini.Repository.AziendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AziendaService {
    
    @Autowired
    private AziendaRepository aziendaRepository;
    
    // Ottieni tutte le aziende
    public List<Azienda> tutteLeAziende() {
        return aziendaRepository.findAll();
    }
    
    // Trova azienda per ID
    public Azienda trovaPerID(Integer id) {
        return aziendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Azienda non trovata!"));
    }
    
    // Crea nuova azienda (per admin)
    public Azienda creaAzienda(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new RuntimeException("Il nome dell'azienda è obbligatorio!");
        }
        
        Azienda nuovaAzienda = new Azienda();
        nuovaAzienda.setNome(nome);
        
        return aziendaRepository.save(nuovaAzienda);
    }
    
    // Aggiorna azienda (per admin)
    public Azienda aggiornaAzienda(Integer id, String nuovoNome) {
        Azienda azienda = trovaPerID(id);
        
        if (nuovoNome != null && !nuovoNome.isEmpty()) {
            azienda.setNome(nuovoNome);
        }
        
        return aziendaRepository.save(azienda);
    }
    
    // Elimina azienda (per admin)
    public void eliminaAzienda(Integer id) {
        if (!aziendaRepository.existsById(id)) {
            throw new RuntimeException("Azienda non trovata!");
        }
        
        aziendaRepository.deleteById(id);
    }
}