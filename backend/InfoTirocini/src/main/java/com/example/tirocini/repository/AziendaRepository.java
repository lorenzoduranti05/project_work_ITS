package com.example.tirocini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tirocini.model.Azienda;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Integer> {
 
}