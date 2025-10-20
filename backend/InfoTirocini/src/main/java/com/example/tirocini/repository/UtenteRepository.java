package com.example.tirocini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tirocini.model.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Utente findByMail(String mail);
    
    boolean existsByMail(String mail);
}