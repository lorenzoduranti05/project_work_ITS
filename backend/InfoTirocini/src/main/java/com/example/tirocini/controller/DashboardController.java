package com.example.tirocini.controller;

// Import necessari per Lavoro e List
import com.example.tirocini.model.Lavoro;
import com.example.tirocini.repository.LavoroRepository;
import java.util.List;

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

    // Iniettiamo il LavoroRepository
    @Autowired
    private LavoroRepository lavoroRepository;

    @GetMapping("/home")
    public String mostraDashboard(Authentication authentication, Model model) {
        String emailUtente = authentication.getName();
        Utente utente = utenteRepository.findByMail(emailUtente);

        model.addAttribute("utente", utente); 
        
        // Recupera tutti i lavori dal DB
        List<Lavoro> lavori = lavoroRepository.findAll();
        
        // Passa la lista "lavori" al template
        model.addAttribute("lavori", lavori);
        
        return "dashboard_cards";            
    }
}