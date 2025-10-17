package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Competenza;
import com.example.InfoTirocini.Repository.CompetenzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompetenzaService {
    
    @Autowired
    private CompetenzaRepository competenzaRepository;
    
    // Ottieni tutte le competenze
    public List<Competenza> tutteLeCompetenze() {
        return competenzaRepository.findAll();
    }
    
    // Trova competenza per ID
    public Competenza trovaPerID(Integer id) {
        return competenzaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Competenza non trovata!"));
    }
    
    // Crea nuova competenza (per admin)
    public Competenza creaCompetenza(String nome, String tipoCompetenza) {
        if (nome == null || nome.isEmpty()) {
            throw new RuntimeException("Il nome della competenza è obbligatorio!");
        }
        
        if (tipoCompetenza == null || 
            (!tipoCompetenza.equals("acquisita") && !tipoCompetenza.equals("richiesta"))) {
            throw new RuntimeException("Tipo competenza deve essere 'acquisita' o 'richiesta'!");
        }
        
        Competenza nuovaCompetenza = new Competenza();
        nuovaCompetenza.setNome(nome);
        nuovaCompetenza.setTipoCompetenza(tipoCompetenza);
        
        return competenzaRepository.save(nuovaCompetenza);
    }
    
    // Elimina competenza (per admin)
    public void eliminaCompetenza(Integer id) {
        if (!competenzaRepository.existsById(id)) {
            throw new RuntimeException("Competenza non trovata!");
        }
        
        competenzaRepository.deleteById(id);
    }
}