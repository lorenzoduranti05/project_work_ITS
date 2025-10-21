package com.example.tirocini.service;

import com.example.tirocini.dto.*;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public RispostaLoginDTO registraUtente(RegistrazioneDTO dto) {

        if (utenteRepository.existsByMail(dto.getMail())) {
            throw new RuntimeException("Email già registrata");
        }


        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new RuntimeException("La password deve contenere almeno 6 caratteri.");
        }

        Utente nuovoUtente = new Utente();

        nuovoUtente.setNome(dto.getNome());
        nuovoUtente.setCognome(dto.getCognome());

        nuovoUtente.setMail(dto.getMail());

        String passwordHash = passwordEncoder.encode(dto.getPassword());
        nuovoUtente.setPassword(passwordHash);


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


        if (!passwordEncoder.matches(dto.getPassword(), utente.getPassword())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }


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
        profilo.setPassword("********"); 

        return profilo;
    }

    public ProfiloDTO aggiornaProfilo(Integer id, ProfiloDTO dto) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (dto.getPasswordAttuale() == null || dto.getPasswordAttuale().isBlank()) {
            throw new RuntimeException("È richiesta la password attuale per salvare le modifiche.");
        }
        if (!passwordEncoder.matches(dto.getPasswordAttuale(), utente.getPassword())) {
            throw new RuntimeException("Password attuale non corretta.");
        }

        boolean modificato = false;
        if (dto.getNome() != null && !dto.getNome().equals(utente.getNome())) {
            utente.setNome(dto.getNome());
            modificato = true;
        }
        if (dto.getCognome() != null && !dto.getCognome().equals(utente.getCognome())) {
            utente.setCognome(dto.getCognome());
            modificato = true;
        }
        if (dto.getMail() != null && !dto.getMail().equals(utente.getMail())) {
            if (utenteRepository.existsByMail(dto.getMail())) {
                throw new RuntimeException("Email già in uso da un altro account.");
            }
            utente.setMail(dto.getMail());
            modificato = true;
        }

        String nuovaPassword = dto.getPassword();
        String confermaNuovaPassword = dto.getConfermaPassword();

        boolean vuoleCambiarePassword = nuovaPassword != null && !nuovaPassword.isBlank();

        if (vuoleCambiarePassword) {
            if (nuovaPassword.length() < 6) {
                 throw new RuntimeException("La nuova password deve avere almeno 6 caratteri.");
            }
            if (confermaNuovaPassword == null || !nuovaPassword.equals(confermaNuovaPassword)) {
                throw new RuntimeException("Le nuove password non coincidono.");
            }
            utente.setPassword(passwordEncoder.encode(nuovaPassword));
            modificato = true;
        }

        if (modificato) {
            utenteRepository.save(utente);
        }

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