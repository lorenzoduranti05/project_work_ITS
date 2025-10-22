package com.example.tirocini.controller;

import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.UtenteRepository;
// Importa CandidaturaService se lo crei separatamente
// import com.example.tirocini.service.CandidaturaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // Opzionale per sicurezza a livello di metodo
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
// @PreAuthorize("hasRole('ADMIN')") // Puoi usarlo per proteggere l'intera classe
public class AdminController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    // Inietta CandidaturaService se sposti la logica lì
    // @Autowired
    // private CandidaturaService candidaturaService;

    @GetMapping("/profilo")
    public String mostraProfiloAdmin(@AuthenticationPrincipal Utente admin, Model model) {
        if (admin == null || !"ADMIN".equals(admin.getRuolo())) {
            return "redirect:/accesso?error=unauthorized";
        }

        model.addAttribute("admin", admin);

        List<Candidatura> candidatureDaGestire = candidaturaRepository.findAll().stream()
                .filter(c -> "Inviata".equalsIgnoreCase(c.getStato()))
                .collect(Collectors.toList());

        model.addAttribute("candidature", candidatureDaGestire);

        return "adminprofile";
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
}