package com.example.tirocini.service;

import com.example.tirocini.dto.*;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 1. Importa il PasswordEncoder di Spring Security
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    // 2. Inietta il PasswordEncoder (che hai definito come @Bean)
    @Autowired
    private PasswordEncoder passwordEncoder;

    public RispostaLoginDTO registraUtente(RegistrazioneDTO dto) {
        // 1. Controlla se l'email esiste già
        if (utenteRepository.existsByMail(dto.getMail())) {
            throw new RuntimeException("Email già registrata");
        }

        // Controlla se la password è nulla o più corta di 6 caratteri
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new RuntimeException("La password deve contenere almeno 6 caratteri.");
        }

        Utente nuovoUtente = new Utente();

        // Corretto per usare i campi separati dal DTO
        nuovoUtente.setNome(dto.getNome());
        nuovoUtente.setCognome(dto.getCognome());

        nuovoUtente.setMail(dto.getMail());
        
        // --- MODIFICA CHIAVE (Registrazione) ---
        // Codifica la password prima di salvarla nel database
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        nuovoUtente.setPassword(passwordHash);
        // --- FINE MODIFICA ---

        nuovoUtente.setRuolo("USER");
        Utente salvato = utenteRepository.save(nuovoUtente);

        RispostaLoginDTO risposta = new RispostaLoginDTO();
        risposta.setId(salvato.getId());
        risposta.setNome(salvato.getNome());
        risposta.setCognome(salvato.getCognome());
        risposta.setMail(salvato.getMail());
        risposta.setRuolo(salvato.getRuolo());
        risposta.setMessaggio("Registrazione completata con successo");

        return risposta;
    }

    public RispostaLoginDTO login(LoginDTO dto) {
        Utente utente = utenteRepository.findByMail(dto.getMail());

        if (utente == null) {
            throw new RuntimeException("USER_NOT_FOUND");
        }

        // --- MODIFICA CHIAVE (Login) ---
        // Confronta la password in chiaro (dal DTO) con l'hash salvato (dal DB)
        if (!passwordEncoder.matches(dto.getPassword(), utente.getPassword())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }
        // --- FINE MODIFICA ---

        RispostaLoginDTO risposta = new RispostaLoginDTO();
        risposta.setId(utente.getId());
        risposta.setNome(utente.getNome());
        risposta.setCognome(utente.getCognome());
        risposta.setMail(utente.getMail());
        risposta.setRuolo(utente.getRuolo());
        risposta.setMessaggio("Login effettuato con successo");

        return risposta;
    }

    public ProfiloDTO getProfilo(Integer id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        ProfiloDTO profilo = new ProfiloDTO();
        profilo.setNome(utente.getNome());
        profilo.setCognome(utente.getCognome());
        profilo.setMail(utente.getMail());
        profilo.setPassword("********"); // Corretto, non esporre mai la password

        return profilo;
    }

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
            if (!utente.getMail().equals(dto.getMail()) && utenteRepository.existsByMail(dto.getMail())) {
                throw new RuntimeException("Email già in uso");
            }
            utente.setMail(dto.getMail());
        }
        
        // --- MODIFICA CHIAVE (Aggiornamento Password) ---
        // Se la password è stata cambiata (non è "********" e non è vuota)
        if (dto.getPassword() != null && !dto.getPassword().isBlank() && !dto.getPassword().equals("********")) {
            // Codifica la nuova password prima di salvarla
            utente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        // --- FINE MODIFICA ---

        utenteRepository.save(utente);

        return getProfilo(id);
    }

    public void eliminaAccount(Integer id) {
        if (!utenteRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato");
        }
        utenteRepository.deleteById(id);
    }

    public List<Utente> trovaTuttiGliUtenti() {
        return utenteRepository.findAll();
    }

    public Utente cambiaRuolo(Integer id, String nuovoRuolo) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        utente.setRuolo(nuovoRuolo);
        return utenteRepository.save(utente);
    }
}