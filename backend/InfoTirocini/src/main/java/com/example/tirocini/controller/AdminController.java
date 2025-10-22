package com.example.tirocini.controller;

import com.example.tirocini.dto.ProfiloDTO;
import com.example.tirocini.model.Azienda;
import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Competenza;
import com.example.tirocini.model.Lavoro;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.AziendaRepository;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.CompetenzaRepository;
import com.example.tirocini.repository.LavoroRepository;
import com.example.tirocini.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set; // Assicurati di importare Set se usi Set nel DTO
import java.util.HashSet; // Esempio se usi HashSet

// La classe rimane Controller perché gestisce sia view Thymeleaf che API REST
@Controller
@RequestMapping("/admin") // Mapping base per le view
public class AdminController {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CandidaturaRepository candidaturaRepository;
    @Autowired
    private LavoroRepository lavoroRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private CompetenzaRepository competenzaRepository;

    // --- Metodi esistenti per le view Thymeleaf ---

    @GetMapping("/profilo")
    public String mostraProfiloAdmin(@AuthenticationPrincipal Utente admin, Model model) {
        if (admin == null || !"ADMIN".equals(admin.getRuolo())) {
            return "redirect:/accesso?error=unauthorized";
        }
        model.addAttribute("admin", admin);

        if (!model.containsAttribute("profiloDTO")) {
            ProfiloDTO dto = new ProfiloDTO();
            // Evita NullPointerException se BindingResult non è presente
            if (!model.containsAttribute("org.springframework.validation.BindingResult.profiloDTO")) {
                 dto.setNome(admin.getNome());
                 dto.setCognome(admin.getCognome());
                 dto.setMail(admin.getMail());
             }
            model.addAttribute("profiloDTO", dto);
         }

        List<Candidatura> candidatureDaGestire = candidaturaRepository.findAll().stream()
                .filter(c -> "Inviata".equalsIgnoreCase(c.getStato()))
                .collect(Collectors.toList());
        model.addAttribute("candidature", candidatureDaGestire);

        return "adminprofile";
    }


    @GetMapping("/dashboard")
    public String mostraAdminDashboard(@AuthenticationPrincipal Utente admin, Model model) {
        if (admin == null || !"ADMIN".equals(admin.getRuolo())) {
            return "redirect:/accesso?error=unauthorized";
        }
        model.addAttribute("admin", admin);

        List<Lavoro> lavori = lavoroRepository.findAll();
        List<Azienda> aziende = aziendaRepository.findAll();
        List<Competenza> competenze = competenzaRepository.findAll();

        model.addAttribute("lavori", lavori);
        model.addAttribute("aziendeDisponibili", aziende);
        model.addAttribute("competenzeDisponibili", competenze);

        return "admindashboard";
    }

    @PostMapping("/candidature/accetta/{id}")
    public String accettaCandidatura(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Candidatura> candidaturaOpt = candidaturaRepository.findById(id);
            if (candidaturaOpt.isPresent()) {
                Candidatura candidatura = candidaturaOpt.get();
                candidatura.setStato("Accettata");
                candidaturaRepository.save(candidatura);
                redirectAttributes.addFlashAttribute("successMessage", "Candidatura accettata con successo.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Candidatura non trovata.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'accettazione della candidatura.");
        }
        return "redirect:/admin/profilo";
    }

    @PostMapping("/candidature/rifiuta/{id}")
    public String rifiutaCandidatura(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
         try {
            Optional<Candidatura> candidaturaOpt = candidaturaRepository.findById(id);
            if (candidaturaOpt.isPresent()) {
                Candidatura candidatura = candidaturaOpt.get();
                candidatura.setStato("Rifiutata");
                candidaturaRepository.save(candidatura);
                redirectAttributes.addFlashAttribute("successMessage", "Candidatura rifiutata con successo.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Candidatura non trovata.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante il rifiuto della candidatura.");
        }
        return "redirect:/admin/profilo";
    }

    // --- Nuovi Endpoint REST per gestione Lavori ---

    // Nota: Il mapping qui è relativo a "/admin", quindi l'URL completo sarà /admin/api/lavori
    @PostMapping("/api/lavori")
    @ResponseBody // Indica che il ritorno è il corpo della risposta, non una view
    public ResponseEntity<?> creaLavoro(@RequestBody LavoroAdminDTO lavoroDTO) {
        try {
            Lavoro nuovoLavoro = new Lavoro();
            nuovoLavoro.setNome(lavoroDTO.getNome());
            nuovoLavoro.setDescrizione(lavoroDTO.getDescrizione());
            nuovoLavoro.setDurata(lavoroDTO.getDurata());
            nuovoLavoro.setOrari(lavoroDTO.getOrari());
            // Imposta un'immagine di default se non fornita
            nuovoLavoro.setImageUrl(lavoroDTO.getImageUrl() != null ? lavoroDTO.getImageUrl() : "default-image.jpg");

            // Trova l'azienda
            Azienda azienda = aziendaRepository.findById(lavoroDTO.getAziendaId())
                    .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + lavoroDTO.getAziendaId()));
            nuovoLavoro.setAzienda(azienda);

            // Trova e associa le competenze
            if (lavoroDTO.getCompetenzeIds() != null && !lavoroDTO.getCompetenzeIds().isEmpty()) {
                List<Competenza> competenze = competenzaRepository.findAllById(lavoroDTO.getCompetenzeIds());
                nuovoLavoro.setCompetenze(competenze);
            }

            lavoroRepository.save(nuovoLavoro);
            // Restituisce 201 Created con un messaggio
            return ResponseEntity.status(HttpStatus.CREATED).body("Annuncio creato con successo!");
        } catch (Exception e) {
            // Restituisce 400 Bad Request in caso di errore
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nella creazione dell'annuncio: " + e.getMessage());
        }
    }

    // URL completo: /admin/api/lavori/{id}
    @PutMapping("/api/lavori/{id}")
    @ResponseBody
    public ResponseEntity<?> aggiornaLavoro(@PathVariable Integer id, @RequestBody LavoroAdminDTO lavoroDTO) {
        try {
            // Cerca il lavoro esistente
            Lavoro lavoroEsistente = lavoroRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lavoro non trovato con ID: " + id));

            // Aggiorna i campi
            lavoroEsistente.setNome(lavoroDTO.getNome());
            lavoroEsistente.setDescrizione(lavoroDTO.getDescrizione());
            lavoroEsistente.setDurata(lavoroDTO.getDurata());
            lavoroEsistente.setOrari(lavoroDTO.getOrari());
            lavoroEsistente.setImageUrl(lavoroDTO.getImageUrl() != null ? lavoroDTO.getImageUrl() : "default-image.jpg");

            // Aggiorna l'azienda
            Azienda azienda = aziendaRepository.findById(lavoroDTO.getAziendaId())
                    .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + lavoroDTO.getAziendaId()));
            lavoroEsistente.setAzienda(azienda);

            // Aggiorna le competenze
             if (lavoroDTO.getCompetenzeIds() != null) {
                 List<Competenza> competenze = competenzaRepository.findAllById(lavoroDTO.getCompetenzeIds());
                 lavoroEsistente.setCompetenze(competenze); // Sovrascrive le competenze esistenti
             } else {
                 lavoroEsistente.setCompetenze(null); // O new ArrayList<>() se preferisci lista vuota
             }


            lavoroRepository.save(lavoroEsistente);
            return ResponseEntity.ok("Annuncio aggiornato con successo!"); // Restituisce 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nell'aggiornamento dell'annuncio: " + e.getMessage());
        }
    }

    // URL completo: /admin/api/lavori/{id}
    @DeleteMapping("/api/lavori/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminaLavoro(@PathVariable Integer id) {
        try {
            // Controlla se l'annuncio esiste prima di tentare l'eliminazione
            if (!lavoroRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Annuncio non trovato con ID: " + id); // Restituisce 404 Not Found
            }
            // Qui potresti voler controllare se ci sono candidature associate prima di eliminare
            // Se sì, potresti volerle eliminare prima o impedire l'eliminazione del lavoro

            lavoroRepository.deleteById(id);
            return ResponseEntity.ok("Annuncio eliminato con successo!"); // Restituisce 200 OK
        } catch (Exception e) {
             // Gestisce altri errori, es. problemi di database
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'annuncio: " + e.getMessage()); // Restituisce 500 Internal Server Error
        }
    }


    // --- DTO Interno per gestire i dati JSON delle API ---
    // Mettilo alla fine della classe AdminController o in un file separato nel package dto
    public static class LavoroAdminDTO {
        private Integer id; // Utile per l'aggiornamento, non necessario per la creazione
        private String nome;
        private String descrizione;
        private Integer aziendaId;
        private String durata;
        private String orari;
        private String imageUrl;
        private List<Integer> competenzeIds; // Lista degli ID delle competenze selezionate

        // Getters e Setters (necessari per la deserializzazione JSON)
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getDescrizione() { return descrizione; }
        public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
        public Integer getAziendaId() { return aziendaId; }
        public void setAziendaId(Integer aziendaId) { this.aziendaId = aziendaId; }
        public String getDurata() { return durata; }
        public void setDurata(String durata) { this.durata = durata; }
        public String getOrari() { return orari; }
        public void setOrari(String orari) { this.orari = orari; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public List<Integer> getCompetenzeIds() { return competenzeIds; }
        public void setCompetenzeIds(List<Integer> competenzeIds) { this.competenzeIds = competenzeIds; }
    }
}