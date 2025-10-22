package com.example.tirocini.service;

import com.example.tirocini.dto.*;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Path fileStorageLocation;

    public UtenteService() {
        this.fileStorageLocation = Paths.get("src/main/resources/static/immagini/profili").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Impossibile creare la directory per l'upload delle immagini.", ex);
        }
    }


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
        nuovoUtente.setProfileImageUrl("/immagini/icona.png"); // Immagine di default
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

    public ProfiloDTO aggiornaProfilo(Integer id, ProfiloDTO dto, MultipartFile profileImageFile) {
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
            // Verifica se l'email è già usata da *un altro* utente
            Utente utenteEsistenteConEmail = utenteRepository.findByMail(dto.getMail());
            if (utenteEsistenteConEmail != null && !utenteEsistenteConEmail.getId().equals(id)) {
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

        // Gestione upload immagine
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
             try {
                 String fileName = storeFile(profileImageFile, id);
                 // Salva il percorso relativo accessibile dal web
                 String fileUrl = "/immagini/profili/" + fileName;
                 utente.setProfileImageUrl(fileUrl);
                 modificato = true;
             } catch (IOException ex) {
                 // Gestisci l'errore, magari loggalo
                 System.err.println("Errore nel salvataggio dell'immagine profilo: " + ex.getMessage());
                 // Potresti decidere di non bloccare l'aggiornamento del profilo per un errore sull'immagine
                 // throw new RuntimeException("Impossibile salvare l'immagine del profilo.", ex);
             }
         }

        if (modificato) {
            utenteRepository.save(utente);
        }

        return getProfilo(id); // Ritorna il DTO aggiornato (senza password)
    }

     // Metodo helper per salvare il file
     private String storeFile(MultipartFile file, Integer userId) throws IOException {
         String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
         String fileExtension = "";
         try {
             // Estrai l'estensione del file
             fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
         } catch (Exception e) {
             fileExtension = ""; // Nessuna estensione trovata
         }
         // Crea un nome file univoco per evitare sovrascritture e problemi di cache
         String fileName = "user_" + userId + "_" + UUID.randomUUID().toString() + fileExtension;

         Path targetLocation = this.fileStorageLocation.resolve(fileName);
         try (InputStream inputStream = file.getInputStream()) {
             Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
         }

         return fileName; // Ritorna solo il nome del file salvato
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