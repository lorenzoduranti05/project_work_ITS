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

        String nomeCompleto = dto.getNome();
        String nome = nomeCompleto;
        String cognome = "";


        int primoSpazio = nomeCompleto.trim().indexOf(" ");
        if (primoSpazio != -1) {
            nome = nomeCompleto.substring(0, primoSpazio).trim();
            cognome = nomeCompleto.substring(primoSpazio + 1).trim();
        }

        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setMail(dto.getMail());
        nuovoUtente.setPassword(dto.getPassword());
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

        if (!utente.getPassword().equals(dto.getPassword())) {
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
        if (dto.getPassword() != null && !dto.getPassword().equals("********")) {
            utente.setPassword(dto.getPassword());
        }

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