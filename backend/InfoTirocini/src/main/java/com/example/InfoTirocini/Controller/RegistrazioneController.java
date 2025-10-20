package com.example.InfoTirocini.Controller;

import com.example.InfoTirocini.Service.UtenteService;
import com.example.InfoTirocini.dto.RegistrazioneDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrazioneController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/registrazione")
    public String mostraPaginaRegistrazione(Model model) {
        model.addAttribute("registrazioneDTO", new RegistrazioneDTO());
        return "registrazione";
    }

    @PostMapping("/registrazione")
    public String processaRegistrazione(
            @Valid @ModelAttribute("registrazioneDTO") RegistrazioneDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "registrazione";
        }

        if (!dto.getPassword().equals(dto.getConfermaPassword())) {
            model.addAttribute("error", "Le password non coincidono. Riprova.");
            return "registrazione";
        }

        try {
            utenteService.registraUtente(dto);

            redirectAttributes.addFlashAttribute("success", "Registrazione completata! Ora puoi accedere.");
            return "redirect:/accesso";

        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registrazione";
        }
    }
}