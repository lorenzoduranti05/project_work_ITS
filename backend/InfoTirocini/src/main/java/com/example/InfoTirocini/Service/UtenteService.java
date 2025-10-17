package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Utente;
import com.example.InfoTirocini.Repository.UtenteRepository;
import com.example.InfoTirocini.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {
    
    @Autowired
    private UtenteRepository utenteRepository;
    
    // Registrazione nuovo utente
    public RispostaLoginDTO registraUtente(RegistrazioneDTO dto) {
        // Controlla se email già esistente
        List<Utente> utenti = utenteRepository.findAll();
        for (Utente u : utenti) {
            if (u.getMail().equals(dto.getMail())) {
                throw new RuntimeException("Email già registrata");
            }
        }
        
        // Crea nuovo utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(dto.getNome());
        nuovoUtente.setCognome(dto.getCognome());
        nuovoUtente.setMail(dto.getMail());
        nuovoUtente.setPassword(dto.getPassword());
        nuovoUtente.setRuolo("USER");
        
        Utente salvato = utenteRepository.save(nuovoUtente);
        
        // Prepara risposta
        RispostaLoginDTO risposta = new RispostaLoginDTO();
        risposta.setId(salvato.getId());
        risposta.setNome(salvato.getNome());
        risposta.setCognome(salvato.getCognome());
        risposta.setMail(salvato.getMail());
        risposta.setRuolo(salvato.getRuolo());
        risposta.setMessaggio("Registrazione completata con successo");
        
        return risposta;
    }
    
    // Login utente
    public RispostaLoginDTO login(LoginDTO dto) {
        List<Utente> utenti = utenteRepository.findAll();
        Utente utente = null;
        
        for (Utente u : utenti) {
            if (u.getMail().equals(dto.getMail())) {
                utente = u;
                break;
            }
        }
        
        if (utente == null) {
            throw new RuntimeException("Credenziali non valide");
        }
        
        if (!utente.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Credenziali non valide");
        }
        
        // Prepara risposta
        RispostaLoginDTO risposta = new RispostaLoginDTO();
        risposta.setId(utente.getId());
        risposta.setNome(utente.getNome());
        risposta.setCognome(utente.getCognome());
        risposta.setMail(utente.getMail());
        risposta.setRuolo(utente.getRuolo());
        risposta.setMessaggio("Login effettuato con successo");
        
        return risposta;
    }
    
    // Ottieni profilo utente
    public ProfiloDTO getProfilo(Integer id) {
        Utente utente = utenteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        ProfiloDTO profilo = new ProfiloDTO();
        profilo.setNome(utente.getNome());
        profilo.setCognome(utente.getCognome());
        profilo.setMail(utente.getMail());
        profilo.setPassword("********");
        
        return profilo;
    }
    
    // Aggiorna profilo utente
    public ProfiloDTO aggiornaProfilo(Integer id, ProfiloDTO dto) {
        Utente utente = utenteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        if (dto.getNome() != null) {
            utente.setNome(dto.getNome());
        }
        if (dto.getCognome() != null) {
            utente.setCognome(dto.getCognome());
        }
        if (dto.getMail() != null) {
            // Verifica che la nuova email non sia già in uso
            if (!utente.getMail().equals(dto.getMail())) {
                List<Utente> utenti = utenteRepository.findAll();
                for (Utente u : utenti) {
                    if (u.getMail().equals(dto.getMail())) {
                        throw new RuntimeException("Email già in uso");
                    }
                }
            }
            utente.setMail(dto.getMail());
        }
        if (dto.getPassword() != null && !dto.getPassword().equals("********")) {
            utente.setPassword(dto.getPassword());
        }
        
        utenteRepository.save(utente);
        
        return getProfilo(id);
    }
    
    // Elimina account
    public void eliminaAccount(Integer id) {
        if (!utenteRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato");
        }
        utenteRepository.deleteById(id);
    }
    
    // Trova tutti gli utenti
    public List<Utente> trovaTuttiGliUtenti() {
        return utenteRepository.findAll();
    }
    
    // Cambia ruolo utente
    public Utente cambiaRuolo(Integer id, String nuovoRuolo) {
        Utente utente = utenteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        utente.setRuolo(nuovoRuolo);
        return utenteRepository.save(utente);
    }
}