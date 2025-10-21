package com.example.tirocini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tirocini.model.Candidatura;
import com.example.tirocini.model.Lavoro;
import com.example.tirocini.model.Utente;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Integer> {
    List<Candidatura> findByUtenteId(Integer utenteId);
    
    List<Candidatura> findByLavoroId(Integer lavoroId);
    
    boolean existsByUtenteIdAndLavoroId(Integer utenteId, Integer lavoroId);
    Optional<Candidatura> findByUtenteAndLavoro(Utente utente, Lavoro lavoro);
}