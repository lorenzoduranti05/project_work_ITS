package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Service.UtenteService;
import com.example.InfoTirocini.dto.LoginDTO;
import com.example.InfoTirocini.dto.RispostaLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    
    @Autowired
    private UtenteService utenteService;
    
    @GetMapping("/accesso")
    public String mostraPaginaAccesso(Model model) {
        if (!model.containsAttribute("loginDTO")) {
            model.addAttribute("loginDTO", new LoginDTO());
        }
        return "accesso";
    }

    @PostMapping("/accesso")
    public String processaLogin(@ModelAttribute("loginDTO") LoginDTO loginDTO, Model model) {
        try {
            utenteService.login(loginDTO);
            return "redirect:/home";

        } catch (RuntimeException ex) {
            String errorMessage;
            if ("USER_NOT_FOUND".equals(ex.getMessage())) {
                errorMessage = "Nessun utente trovato con questa email.";
            } else if ("INVALID_PASSWORD".equals(ex.getMessage())) {
                errorMessage = "Password non corretta. Riprova.";
            } else {
                errorMessage = "Errore durante il login.";
            }

            model.addAttribute("error", errorMessage);
            model.addAttribute("loginDTO", loginDTO); 
            return "accesso"; 
        }
    }
}