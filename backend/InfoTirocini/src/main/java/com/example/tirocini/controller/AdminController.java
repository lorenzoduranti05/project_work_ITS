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


@Controller
@RequestMapping("/admin")
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


    @GetMapping("/profilo")
    public String mostraProfiloAdmin(@AuthenticationPrincipal Utente admin, Model model) {
        if (admin == null || !"ADMIN".equals(admin.getRuolo())) {
            return "redirect:/accesso?error=unauthorized";
        }
        model.addAttribute("admin", admin);

        if (!model.containsAttribute("profiloDTO")) {
            ProfiloDTO dto = new ProfiloDTO();
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
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'accettazione della candidatura: " + e.getMessage());
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
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante il rifiuto della candidatura: " + e.getMessage());
        }
        return "redirect:/admin/profilo";
    }



    @PostMapping("/api/lavori")
    @ResponseBody
    public ResponseEntity<?> creaLavoro(@RequestBody LavoroAdminDTO lavoroDTO) {
        try {
            Lavoro nuovoLavoro = new Lavoro();
            nuovoLavoro.setNome(lavoroDTO.getNome());
            nuovoLavoro.setDescrizione(lavoroDTO.getDescrizione()); 
            nuovoLavoro.setDurata(lavoroDTO.getDurata());
            nuovoLavoro.setOrari(lavoroDTO.getOrari());
            nuovoLavoro.setImageUrl(lavoroDTO.getImageUrl() != null && !lavoroDTO.getImageUrl().isBlank() ? lavoroDTO.getImageUrl() : "default-image.jpg"); // Usa default se vuoto

            Azienda azienda = aziendaRepository.findById(lavoroDTO.getAziendaId())
                    .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + lavoroDTO.getAziendaId()));
            nuovoLavoro.setAzienda(azienda);

            if (lavoroDTO.getCompetenzeIds() != null && !lavoroDTO.getCompetenzeIds().isEmpty()) {
                List<Competenza> competenze = competenzaRepository.findAllById(lavoroDTO.getCompetenzeIds());
                if (competenze.size() != lavoroDTO.getCompetenzeIds().size()) {
                     System.out.println("Attenzione: Alcuni ID competenza non trovati.");
                }
                nuovoLavoro.setCompetenze(competenze);
            }

            lavoroRepository.save(nuovoLavoro);
            return ResponseEntity.status(HttpStatus.CREATED).body("Annuncio creato con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nella creazione dell'annuncio: " + e.getMessage());
        }
    }

    @PutMapping("/api/lavori/{id}")
    @ResponseBody
    public ResponseEntity<?> aggiornaLavoro(@PathVariable Integer id, @RequestBody LavoroAdminDTO lavoroDTO) {
        try {
            Lavoro lavoroEsistente = lavoroRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lavoro non trovato con ID: " + id));

            lavoroEsistente.setNome(lavoroDTO.getNome());
            lavoroEsistente.setDescrizione(lavoroDTO.getDescrizione());
            lavoroEsistente.setDurata(lavoroDTO.getDurata());
            lavoroEsistente.setOrari(lavoroDTO.getOrari());
            lavoroEsistente.setImageUrl(lavoroDTO.getImageUrl() != null && !lavoroDTO.getImageUrl().isBlank() ? lavoroDTO.getImageUrl() : "default-image.jpg");

            Azienda azienda = aziendaRepository.findById(lavoroDTO.getAziendaId())
                    .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + lavoroDTO.getAziendaId()));
            lavoroEsistente.setAzienda(azienda);

             if (lavoroDTO.getCompetenzeIds() != null) {
                 List<Competenza> competenze = competenzaRepository.findAllById(lavoroDTO.getCompetenzeIds());
                  if (competenze.size() != lavoroDTO.getCompetenzeIds().size()) {
                     System.out.println("Attenzione: Alcuni ID competenza non trovati durante l'aggiornamento.");
                  }
                 lavoroEsistente.setCompetenze(competenze);
             } else {
                 lavoroEsistente.setCompetenze(null);
             }

            lavoroRepository.save(lavoroEsistente);
            return ResponseEntity.ok("Annuncio aggiornato con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nell'aggiornamento dell'annuncio: " + e.getMessage());
        }
    }


    @DeleteMapping("/api/lavori/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminaLavoro(@PathVariable Integer id) {
        try {
            if (!lavoroRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Annuncio non trovato con ID: " + id);
            }
           
            lavoroRepository.deleteById(id);
            return ResponseEntity.ok("Annuncio eliminato con successo!");
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'annuncio: " + e.getMessage());
        }
    }


    @PostMapping("/api/aziende")
    @ResponseBody
    public ResponseEntity<?> creaAzienda(@RequestBody AziendaDTO aziendaDTO) {
        try {
            if (aziendaDTO.getNome() == null || aziendaDTO.getNome().isBlank()) {
                return ResponseEntity.badRequest().body("Il nome dell'azienda non può essere vuoto.");
            }
            Azienda nuovaAzienda = new Azienda();
            nuovaAzienda.setNome(aziendaDTO.getNome().trim());
            aziendaRepository.save(nuovaAzienda);
            return ResponseEntity.status(HttpStatus.CREATED).body("Azienda aggiunta con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiunta dell'azienda: " + e.getMessage());
        }
    }

    @PostMapping("/api/competenze")
    @ResponseBody
    public ResponseEntity<?> creaCompetenza(@RequestBody CompetenzaDTO competenzaDTO) {
        try {
             if (competenzaDTO.getNome() == null || competenzaDTO.getNome().isBlank()) {
                return ResponseEntity.badRequest().body("Il nome della competenza non può essere vuoto.");
             }
             if (competenzaDTO.getTipoCompetenza() == null || competenzaDTO.getTipoCompetenza().isBlank()) {
                 return ResponseEntity.badRequest().body("Il tipo di competenza non può essere vuoto.");
             }

             Competenza nuovaCompetenza = new Competenza();
             nuovaCompetenza.setNome(competenzaDTO.getNome().trim());
             nuovaCompetenza.setTipoCompetenza(competenzaDTO.getTipoCompetenza().trim());
             competenzaRepository.save(nuovaCompetenza);
             return ResponseEntity.status(HttpStatus.CREATED).body("Competenza aggiunta con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiunta della competenza: " + e.getMessage());
        }
    }


    public static class LavoroAdminDTO {
        private Integer id;
        private String nome;

        private String descrizione;
        private Integer aziendaId;
        private String durata;
        private String orari;
        private String imageUrl;
        private List<Integer> competenzeIds;

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


    public static class AziendaDTO {
        private String nome;
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    public static class CompetenzaDTO {
        private String nome;
        private String tipoCompetenza;
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getTipoCompetenza() { return tipoCompetenza; }
        public void setTipoCompetenza(String tipoCompetenza) { this.tipoCompetenza = tipoCompetenza; }
    }
}