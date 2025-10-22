package com.example.tirocini.controller;

import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Lavoro;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.LavoroRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private LavoroRepository lavoroRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @GetMapping("/home")
    public String mostraDashboard(Authentication authentication, Model model) {
        String emailUtente = authentication.getName();
        Utente utente = utenteRepository.findByMail(emailUtente);

        model.addAttribute("utente", utente);

        List<Lavoro> lavori = lavoroRepository.findAll();
        model.addAttribute("lavori", lavori);

        if (utente != null) {
            List<Candidatura> tutteCandidature = candidaturaRepository.findByUtenteId(utente.getId());

            List<Candidatura> candidatureInviate = tutteCandidature.stream()
                    .filter(c -> "Inviata".equalsIgnoreCase(c.getStato()))
                    .collect(Collectors.toList());

            List<Candidatura> candidatureAccettate = tutteCandidature.stream()
                    .filter(c -> "Accettata".equalsIgnoreCase(c.getStato()))
                    .collect(Collectors.toList());

            List<Candidatura> candidatureRifiutate = tutteCandidature.stream()
                    .filter(c -> "Rifiutata".equalsIgnoreCase(c.getStato()))
                    .collect(Collectors.toList());

            model.addAttribute("candidatureInviate", candidatureInviate);
            model.addAttribute("inviateCount", candidatureInviate.size());

            model.addAttribute("candidatureAccettate", candidatureAccettate);
            model.addAttribute("accettateCount", candidatureAccettate.size());

            model.addAttribute("candidatureRifiutate", candidatureRifiutate);
            model.addAttribute("rifiutateCount", candidatureRifiutate.size());
        } else {
             model.addAttribute("candidatureInviate", List.of());
             model.addAttribute("inviateCount", 0);
             model.addAttribute("candidatureAccettate", List.of());
             model.addAttribute("accettateCount", 0);
             model.addAttribute("candidatureRifiutate", List.of());
             model.addAttribute("rifiutateCount", 0);
        }

        return "dashboard_cards";
    }
}