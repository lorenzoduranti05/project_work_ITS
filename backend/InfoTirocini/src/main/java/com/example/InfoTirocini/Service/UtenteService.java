package com.example.InfoTirocini.Service;

import com.example.InfoTirocini.Model.Utente;
import com.example.InfoTirocini.Repository.UtenteRepository;
import com.example.InfoTirocini.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UtenteService {
    
    @Autowired
    private UtenteRepository utenteRepository;
    
    // Registrazione nuovo utente (sempre come USER)
    public RispostaLoginDTO registraUtente(RegistrazioneDTO dto) {
        // Controlla se email già esistente
        if (utenteRepository.existsByMail(dto.getMail())) {
            throw new RuntimeException("Email già registrata!");
        }
        
        // Crea nuovo utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(dto.getNome());
        nuovoUtente.setCognome(dto.getCognome());
        nuovoUtente.setMail(dto.getMail());
        nuovoUtente.setPassword(dto.getPassword()); // In produzione, criptare!
        nuovoUtente.setRuolo("USER"); // Sempre USER per chi si registra
        
        // Salva nel database
        Utente salvato = utenteRepository.save(nuovoUtente);
        
        // Restituisci risposta DTO
        return new RispostaLoginDTO(
            salvato.getId(),
            salvato.getNome(),
            salvato.getCognome(),
            salvato.getMail(),
            salvato.getRuolo(),
            "Registrazione completata con successo!"
        );
    }
    
    // Login utente
    public RispostaLoginDTO login(LoginDTO dto) {
        // Cerca utente per email
        Utente utente = utenteRepository.findByMail(dto.getMail());
        
        if (utente == null) {
            throw new RuntimeException("Email non trovata!");
        }
        
        // Verifica password
        if (!utente.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Password errata!");
        }
        
        // Login riuscito - restituisci dati utente
        return new RispostaLoginDTO(
            utente.getId(),
            utente.getNome(),
            utente.getCognome(),
            utente.getMail(),
            utente.getRuolo(), // USER o ADMIN dal database
            "Login effettuato con successo!"
        );
    }
    
    // Trova utente per ID
    public Utente trovaPerID(Integer id) {
        return utenteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utente con ID " + id + " non trovato!"));
    }
    
    // Trova utente per email
    public Utente trovaPerEmail(String email) {
        Utente utente = utenteRepository.findByMail(email);
        if (utente == null) {
            throw new RuntimeException("Utente non trovato!");
        }
        return utente;
    }
    
    // Aggiorna profilo utente
    public Utente aggiornaProfilo(Integer id, ProfiloDTO dto) {
        Utente utente = trovaPerID(id);
        
        // Aggiorna solo i campi forniti (non null)
        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            utente.setNome(dto.getNome());
        }
        
        if (dto.getCognome() != null && !dto.getCognome().isEmpty()) {
            utente.setCognome(dto.getCognome());
        }
        
        if (dto.getMail() != null && !dto.getMail().isEmpty()) {
            // Verifica che la nuova email non sia già usata da altri
            if (!dto.getMail().equals(utente.getMail())) {
                if (utenteRepository.existsByMail(dto.getMail())) {
                    throw new RuntimeException("Email già in uso da un altro utente!");
                }
                utente.setMail(dto.getMail());
            }
        }
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            utente.setPassword(dto.getPassword()); // In produzione, criptare!
        }
        
        // Salva modifiche
        return utenteRepository.save(utente);
    }
    
    // Ottieni tutti gli utenti (per admin)
    public List<Utente> tuttiGliUtenti() {
        return utenteRepository.findAll();
    }
    
    // Verifica se un utente è admin
    public boolean isAdmin(Integer userId) {
        Utente utente = trovaPerID(userId);
        return "ADMIN".equals(utente.getRuolo());
    }
}