package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Azienda;
import com.example.InfoTirocini.Model.Lavoro;
import com.example.InfoTirocini.Repository.AziendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AziendaService {
    
    @Autowired
    private AziendaRepository aziendaRepository;
    
    // Crea nuova azienda
    public Azienda creaAzienda(String nome) {
        Azienda azienda = new Azienda();
        azienda.setNome(nome);
        return aziendaRepository.save(azienda);
    }
    
    // Ottieni tutte le aziende
    public List<Azienda> trovaTutteLeAziende() {
        return aziendaRepository.findAll();
    }
    
    // Trova azienda per ID
    public Optional<Azienda> trovaAziendaPerId(Integer id) {
        return aziendaRepository.findById(id);
    }
    
    // Aggiorna azienda
    public Azienda aggiornaAzienda(Integer id, String nuovoNome) {
        Azienda azienda = aziendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        
        azienda.setNome(nuovoNome);
        return aziendaRepository.save(azienda);
    }
    
    // Elimina azienda
    public void eliminaAzienda(Integer id) {
        if (!aziendaRepository.existsById(id)) {
            throw new RuntimeException("Azienda non trovata");
        }
        aziendaRepository.deleteById(id);
    }
    
    // Trova azienda per nome
    public Optional<Azienda> trovaAziendaPerNome(String nome) {
        List<Azienda> tutte = aziendaRepository.findAll();
        
        for (Azienda a : tutte) {
            if (a.getNome() != null && a.getNome().equals(nome)) {
                return Optional.of(a);
            }
        }
        
        return Optional.empty();
    }
    
    // Ottieni lavori di un'azienda
    public List<Lavoro> getLavoriAzienda(Integer aziendaId) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
            .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        return azienda.getLavori();
    }
    
    // Conta lavori per azienda
    public int contaLavoriPerAzienda(Integer aziendaId) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
            .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        return azienda.getLavori() != null ? azienda.getLavori().size() : 0;
    }
}