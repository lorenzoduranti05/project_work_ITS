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

    
    
}