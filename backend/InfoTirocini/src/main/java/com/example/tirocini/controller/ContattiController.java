package com.example.tirocini.controller;

import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContattiController {

    @Autowired
    private UtenteRepository utenteRepository;

    @GetMapping("/contatti")
    public String mostraPaginaContatti(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String emailUtente = authentication.getName();
            Utente utente = utenteRepository.findByMail(emailUtente);
            model.addAttribute("utente", utente);
        }
        return "contatti"; // Nome del file HTML (es. contatti.html)
    }

    @PostMapping("/invia-contatto")
    public String processaInvioMessaggio(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("oggetto") String oggetto,
            @RequestParam("messaggio") String messaggio,
            RedirectAttributes redirectAttributes) {

        try {
            // Qui dovresti inserire la logica per inviare l'email o salvare il messaggio.
            // Esempio: emailService.inviaEmailContatto(nome, email, oggetto, messaggio);
            System.out.println("Messaggio ricevuto da: " + nome + " (" + email + ")");
            System.out.println("Oggetto: " + oggetto);
            System.out.println("Messaggio: " + messaggio);

            redirectAttributes.addFlashAttribute("messaggioInviato", true);
        } catch (Exception e) {
            System.err.println("Errore durante l'invio del messaggio: " + e.getMessage());
            redirectAttributes.addFlashAttribute("erroreInvio", true);
        }

        return "redirect:/contatti"; // Reindirizza alla pagina contatti
    }
}