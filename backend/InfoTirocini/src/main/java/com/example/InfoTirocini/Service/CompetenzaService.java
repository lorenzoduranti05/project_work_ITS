package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Competenza;
import com.example.InfoTirocini.Model.Lavoro;
import com.example.InfoTirocini.Repository.CompetenzaRepository;
import com.example.InfoTirocini.Repository.LavoroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompetenzaService {
    
    @Autowired
    private CompetenzaRepository competenzaRepository;
    
    @Autowired
    private LavoroRepository lavoroRepository;
    
    // Crea nuova competenza
    public Competenza creaCompetenza(String nome, String tipo) {
        Competenza competenza = new Competenza();
        competenza.setNome(nome);
        competenza.setTipoCompetenza(tipo);
        return competenzaRepository.save(competenza);
    }
    
    // Ottieni tutte le competenze
    public List<Competenza> tutteLeCompetenze() {
        return competenzaRepository.findAll();
    }
    
    // Aggiungi competenze richieste a un lavoro
    public Lavoro aggiungiCompetenzeALavoro(Integer lavoroId, List<Integer> competenzeIds) {
        Lavoro lavoro = lavoroRepository.findById(lavoroId)
            .orElseThrow(() -> new RuntimeException("Lavoro non trovato"));
        
        List<Competenza> competenze = competenzaRepository.findAllById(competenzeIds);
        lavoro.setCompetenzeRichieste(competenze);
        
        return lavoroRepository.save(lavoro);
    }
    
    // Trova lavori che richiedono una specifica competenza
    public List<Lavoro> trovaLavoriPerCompetenza(Integer competenzaId) {
        Competenza competenza = competenzaRepository.findById(competenzaId)
            .orElseThrow(() -> new RuntimeException("Competenza non trovata"));
        return competenza.getLavori();
    }
}