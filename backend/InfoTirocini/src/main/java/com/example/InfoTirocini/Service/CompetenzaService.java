package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Competenza;
import com.example.InfoTirocini.Model.Lavoro;
import com.example.InfoTirocini.Repository.CompetenzaRepository;
import com.example.InfoTirocini.Repository.LavoroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompetenzaService {
    
    @Autowired
    private CompetenzaRepository competenzaRepository;
    
    @Autowired
    private LavoroRepository lavoroRepository;
    
    // Crea nuova competenza
    public Competenza creaCompetenza(String nome, String tipoCompetenza) {
        // Verifica se esiste già
        List<Competenza> esistenti = competenzaRepository.findAll();
        for (Competenza c : esistenti) {
            if (c.getNome() != null && c.getNome().equals(nome)) {
                throw new RuntimeException("Competenza già esistente");
            }
        }
        
        Competenza competenza = new Competenza();
        competenza.setNome(nome);
        competenza.setTipoCompetenza(tipoCompetenza);
        return competenzaRepository.save(competenza);
    }
    
    // Ottieni tutte le competenze
    public List<Competenza> trovaTutteLeCompetenze() {
        return competenzaRepository.findAll();
    }
    
    // Trova competenza per ID
    public Optional<Competenza> trovaCompetenzaPerId(Integer id) {
        return competenzaRepository.findById(id);
    }
    
    // Trova competenze per tipo
    public List<Competenza> trovaCompetenzePerTipo(String tipo) {
        List<Competenza> tutte = competenzaRepository.findAll();
        List<Competenza> risultati = new ArrayList<>();
        
        for (Competenza c : tutte) {
            if (c.getTipoCompetenza() != null && c.getTipoCompetenza().equals(tipo)) {
                risultati.add(c);
            }
        }
        
        return risultati;
    }
    
    // Aggiorna competenza
    public Competenza aggiornaCompetenza(Integer id, String nome, String tipo) {
        Competenza competenza = competenzaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Competenza non trovata"));
        
        if (nome != null) {
            competenza.setNome(nome);
        }
        if (tipo != null) {
            competenza.setTipoCompetenza(tipo);
        }
        
        return competenzaRepository.save(competenza);
    }
    
    // Elimina competenza
    public void eliminaCompetenza(Integer id) {
        if (!competenzaRepository.existsById(id)) {
            throw new RuntimeException("Competenza non trovata");
        }
        competenzaRepository.deleteById(id);
    }
    
    // Trova lavori che richiedono questa competenza
    public List<Lavoro> trovaLavoriPerCompetenza(Integer competenzaId) {
        Competenza competenza = competenzaRepository.findById(competenzaId)
            .orElseThrow(() -> new RuntimeException("Competenza non trovata"));
        return competenza.getLavori();
    }
    
    // Ottieni competenze richieste per un lavoro
    public List<Competenza> getCompetenzePerLavoro(Integer lavoroId) {
        Lavoro lavoro = lavoroRepository.findById(lavoroId)
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato"));
        return lavoro.getCompetenzeRichieste();
    }
}