package com.example.tirocini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tirocini.model.Candidatura;

import java.util.List;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Integer> {
    List<Candidatura> findByUtenteId(Integer utenteId);
    
    List<Candidatura> findByLavoroId(Integer lavoroId);
    
    boolean existsByUtenteIdAndLavoroId(Integer utenteId, Integer lavoroId);
}