package com.example.tirocini.controller;

import com.example.tirocini.dto.ProfiloDTO;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.UtenteRepository; // <-- IMPORTA UtenteRepository
import com.example.tirocini.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProfiloController {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private UtenteService utenteService;

    // --- AGGIUNGI INIEZIONE UTENTEREPOSITORY ---
    @Autowired
    private UtenteRepository utenteRepository;
    // ------------------------------------------

    @GetMapping("/profilo")
    // Rinominato 'utente' in 'principal' per chiarezza
    public String mostraProfilo(@AuthenticationPrincipal Utente principal, Model model) {

        if (principal == null) {
            return "redirect:/accesso?error=unauthorized";
        }

        // --- RICARICA L'UTENTE DAL DATABASE USANDO L'ID DEL PRINCIPAL ---
        // Questo assicura che 'utente' contenga i dati più recenti
        Utente utente = utenteRepository.findById(principal.getId())
                .orElse(principal); // Usa principal come fallback (ma non dovrebbe servire)
        // -------------------------------------------------------------

        // Assicurati che il DTO per il modale usi i dati freschi
        if (!model.containsAttribute("profiloDTO")) {
            ProfiloDTO dto = new ProfiloDTO();
            dto.setNome(utente.getNome()); // Usa utente ricaricato
            dto.setCognome(utente.getCognome()); // Usa utente ricaricato
            dto.setMail(utente.getMail()); // Usa utente ricaricato
            model.addAttribute("profiloDTO", dto);
        }

        // Aggiungi l'utente ricaricato al model per la visualizzazione nella card
        model.addAttribute("utente", utente);

        // Il resto del codice per le candidature rimane uguale...
        try {
            // Usa l'ID dell'utente ricaricato
            var candidature = candidaturaRepository.findByUtenteId(utente.getId());

            long inviateCount = candidature.stream()
                                    .filter(c -> "Inviata".equalsIgnoreCase(c.getStato()))
                                    .count();
            long accettateCount = candidature.stream()
                                    .filter(c -> "Accettata".equalsIgnoreCase(c.getStato()))
                                    .count();
            // Corretto nome variabile: rifiutateCount
            long rifiutateCount = candidature.stream()
                                    .filter(c -> "Rifiutata".equalsIgnoreCase(c.getStato()))
                                    .count();

            model.addAttribute("candidatureInviate", candidature);
            model.addAttribute("inviateCount", inviateCount);
            model.addAttribute("accettateCount", accettateCount);
            // Corretto nome attributo: rifiutateCount
            model.addAttribute("rifiutateCount", rifiutateCount);

        } catch (Exception e) {
             model.addAttribute("errorMessage", "Errore nel caricamento delle candidature.");
        }

        return "Profilo";
    }

    @PostMapping("/profilo/aggiorna")
    public String aggiornaProfilo(
            @ModelAttribute("profiloDTO") ProfiloDTO dto,
            @AuthenticationPrincipal Utente utente, // Qui 'utente' è ancora il principal originale
            RedirectAttributes redirectAttributes) {

        if (utente == null) {
            return "redirect:/accesso";
        }

        try {
            utenteService.aggiornaProfilo(utente.getId(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Profilo aggiornato con successo!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("profiloDTO", dto);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/profilo";
    }
}