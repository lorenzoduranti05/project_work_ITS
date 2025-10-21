package com.example.tirocini.controller;

import com.example.tirocini.dto.ProfiloDTO;
import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.UtenteRepository;
import com.example.tirocini.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfiloController {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private UtenteRepository utenteRepository;

    @GetMapping("/profilo")
    public String mostraProfilo(@AuthenticationPrincipal Utente principal, Model model) {

        if (principal == null) {
            return "redirect:/accesso?error=unauthorized";
        }

        Utente utente = utenteRepository.findById(principal.getId())
                .orElse(principal);

        if (!model.containsAttribute("profiloDTO")) {
            ProfiloDTO dto = new ProfiloDTO();
            dto.setNome(utente.getNome());
            dto.setCognome(utente.getCognome());
            dto.setMail(utente.getMail());
            model.addAttribute("profiloDTO", dto);
        }

        model.addAttribute("utente", utente);

        try {
            List<Candidatura> tutteCandidature = candidaturaRepository.findByUtenteId(utente.getId()); //

            List<Candidatura> candidatureInviate = tutteCandidature.stream()
                    .filter(c -> "Inviata".equalsIgnoreCase(c.getStato()))
                    .collect(Collectors.toList()); //

            List<Candidatura> candidatureAccettate = tutteCandidature.stream()
                    .filter(c -> "Accettata".equalsIgnoreCase(c.getStato()))
                    .collect(Collectors.toList()); //

            List<Candidatura> candidatureRifiutate = tutteCandidature.stream()
                    .filter(c -> "Rifiutata".equalsIgnoreCase(c.getStato()))
                    .collect(Collectors.toList()); //

            model.addAttribute("candidatureInviate", candidatureInviate);
            model.addAttribute("inviateCount", candidatureInviate.size()); //

            model.addAttribute("candidatureAccettate", candidatureAccettate);
            model.addAttribute("accettateCount", candidatureAccettate.size()); //

            model.addAttribute("candidatureRifiutate", candidatureRifiutate);
            model.addAttribute("rifiutateCount", candidatureRifiutate.size()); //

        } catch (Exception e) {
             model.addAttribute("errorMessage", "Errore nel caricamento delle candidature."); //
        }

        return "Profilo"; //
    }

    @PostMapping("/profilo/aggiorna")
    public String aggiornaProfilo(
            @ModelAttribute("profiloDTO") ProfiloDTO dto,
            @AuthenticationPrincipal Utente utente,
            RedirectAttributes redirectAttributes) {

        if (utente == null) {
            return "redirect:/accesso"; //
        }

        try {
            utenteService.aggiornaProfilo(utente.getId(), dto); //
            redirectAttributes.addFlashAttribute("successMessage", "Profilo aggiornato con successo!"); //
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("profiloDTO", dto); //
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage()); //
        }

        return "redirect:/profilo"; //
    }
}