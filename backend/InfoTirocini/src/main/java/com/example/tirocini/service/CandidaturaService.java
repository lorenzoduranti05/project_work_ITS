package com.example.tirocini.service;

import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Lavoro;
import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.CandidaturaRepository;
import com.example.tirocini.repository.LavoroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; 
import java.util.Optional;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private LavoroRepository lavoroRepository;

    public void creaCandidatura(Integer lavoroId, Utente utente) {
        Lavoro lavoro = lavoroRepository.findById(lavoroId)
                .orElseThrow(() -> new RuntimeException("Lavoro non trovato con id: " + lavoroId));

        Optional<Candidatura> existingCandidatura = candidaturaRepository.findByUtenteAndLavoro(utente, lavoro);
        if (existingCandidatura.isPresent()) {
            throw new RuntimeException("Ti sei già candidato per questa offerta.");
        }

        Candidatura nuovaCandidatura = new Candidatura();
        nuovaCandidatura.setUtente(utente);
        nuovaCandidatura.setLavoro(lavoro);
        nuovaCandidatura.setDataCandidatura(LocalDateTime.now()); 
        
        nuovaCandidatura.setStato("Inviata");
        candidaturaRepository.save(nuovaCandidatura);
    }
}