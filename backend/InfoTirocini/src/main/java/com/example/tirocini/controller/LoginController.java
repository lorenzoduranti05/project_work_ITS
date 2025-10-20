package com.example.tirocini.controller;

import com.example.tirocini.dto.LoginDTO;
import com.example.tirocini.dto.RispostaLoginDTO;
import com.example.tirocini.service.UtenteService;

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