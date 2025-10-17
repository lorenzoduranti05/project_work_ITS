package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Service.UtenteService;
import com.example.InfoTirocini.dto.RegistrazioneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrazioneController {

    @Autowired
    private UtenteService utenteService;

    // Metodo per MOSTRARE la pagina di registrazione
    @GetMapping("/registrazione")
    public String mostraPaginaRegistrazione(Model model) {
        model.addAttribute("registrazioneDTO", new RegistrazioneDTO());
        return "registrazione"; // Nome del file HTML
    }

    // Metodo per PROCESSARE i dati inviati dal form
    @PostMapping("/registrazione")
    public String processaRegistrazione(@ModelAttribute("registrazioneDTO") RegistrazioneDTO dto, Model model, RedirectAttributes redirectAttributes) {

        // 1. Controlla che le password coincidano
        if (!dto.getPassword().equals(dto.getConfermaPassword())) {
            model.addAttribute("error", "Le password non coincidono. Riprova.");
            return "registrazione"; // Ritorna alla pagina mostrando l'errore
        }

        try {
            // 2. Chiama il service per registrare l'utente
            utenteService.registraUtente(dto);

            // 3. Se ha successo, reindirizza alla pagina di login con un messaggio
            redirectAttributes.addFlashAttribute("success", "Registrazione completata! Ora puoi accedere.");
            return "redirect:/accesso";

        } catch (RuntimeException ex) {
            // 4. Se l'email esiste già, il service lancerà un'eccezione
            model.addAttribute("error", ex.getMessage()); // Es: "Email già registrata"
            return "registrazione"; // Ritorna alla pagina mostrando l'errore
        }
    }
}