package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Service.UtenteService;
import com.example.InfoTirocini.dto.LoginDTO;
import com.example.InfoTirocini.dto.RispostaLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller  // Nota: @Controller, non @RestController
public class LoginController {
    
    @Autowired
    private UtenteService utenteService;
    
    // Mostra la pagina di accesso
    @GetMapping("/accesso")
    public String mostraPaginaAccesso(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "accesso"; // nome del file HTML senza estensione
    }
}