
package com.example.tirocini.controller;

import com.example.tirocini.dto.ProfiloDTO;
import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.UtenteRepository;
import com.example.tirocini.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfiloController {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private UtenteRepository utenteRepository;

    @GetMapping("/profilo")
    public String mostraProfilo(@AuthenticationPrincipal Utente principal, Model model) {
        if (principal == null) {
            return "redirect:/accesso?error=unauthorized";
        }

        Utente utente = utenteRepository.findById(principal.getId()).orElse(principal);
        model.addAttribute("utente", utente);

        if (!model.containsAttribute("profiloDTO")) {
            ProfiloDTO dto = new ProfiloDTO();
            dto.setNome(utente.getNome());
            dto.setCognome(utente.getCognome());
            dto.setMail(utente.getMail());
            model.addAttribute("profiloDTO", dto);
        }
        try {
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
        } catch (Exception e) {
             model.addAttribute("errorMessage", "Errore nel caricamento delle candidature.");
             model.addAttribute("inviateCount", 0);
             model.addAttribute("accettateCount", 0);
             model.addAttribute("rifiutateCount", 0);
        }

        return "Profilo";
    }


    @PostMapping("/profilo/aggiorna")
    public String aggiornaProfilo(
            @Valid @ModelAttribute("profiloDTO") ProfiloDTO dto,
            BindingResult bindingResult,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            @AuthenticationPrincipal Utente utente,
            RedirectAttributes redirectAttributes) {

        if (utente == null) {
            return "redirect:/accesso";
        }

        boolean vuoleCambiarePassword = dto.getPassword() != null && !dto.getPassword().isBlank();


        if (bindingResult.hasErrors()) {
            boolean ignorePasswordErrors = false;
            if (!vuoleCambiarePassword) {
                long nonPasswordErrorCount = bindingResult.getFieldErrors().stream()
                    .filter(fe -> !fe.getField().equals("password") && !fe.getField().equals("confermaPassword"))
                    .count();
                if (nonPasswordErrorCount == 0) {
                    ignorePasswordErrors = true;
                }
            }

            if (!ignorePasswordErrors) {
                 redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profiloDTO", bindingResult);
                 redirectAttributes.addFlashAttribute("profiloDTO", dto);
                 return "redirect:/profilo";
            }
        }

        if (vuoleCambiarePassword) {
            if (dto.getConfermaPassword() == null || !dto.getPassword().equals(dto.getConfermaPassword())) {
                bindingResult.addError(new FieldError("profiloDTO", "confermaPassword", "Le nuove password non coincidono."));
                 redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profiloDTO", bindingResult);
                 redirectAttributes.addFlashAttribute("profiloDTO", dto);
                 return "redirect:/profilo";
             }
        }

        try {
            utenteService.aggiornaProfilo(utente.getId(), dto, profileImageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Profilo aggiornato con successo!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("profiloDTO", dto);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/profilo";
    }
}