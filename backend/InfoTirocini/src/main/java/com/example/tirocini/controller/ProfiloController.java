package com.example.tirocini.controller;

import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.CandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// Optional: Import logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ProfiloController {

 

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @GetMapping("/profilo")
    public String mostraProfilo(@AuthenticationPrincipal Utente utente, Model model) {

    
        if (utente == null) {
            return "redirect:/accesso?error=unauthorized"; 
        }
        model.addAttribute("utente", utente);

        try {
            var candidature = candidaturaRepository.findByUtenteId(utente.getId());

            long inviateCount = candidature.stream()
                                    .filter(c -> "Inviata".equalsIgnoreCase(c.getStato()))
                                    .count();
            long accettateCount = candidature.stream()
                                    .filter(c -> "Accettata".equalsIgnoreCase(c.getStato()))
                                    .count();
            long rifiateCount = candidature.stream()
                                    .filter(c -> "Rifiutata".equalsIgnoreCase(c.getStato()))
                                    .count();

            model.addAttribute("candidatureInviate", candidature);
            model.addAttribute("inviateCount", inviateCount);
            model.addAttribute("accettateCount", accettateCount);
            model.addAttribute("rifiutateCount", rifiateCount); 

        } catch (Exception e) {
    
             model.addAttribute("errorMessage", "Errore nel caricamento delle candidature.");
        }

        return "Profilo"; 
    }
}