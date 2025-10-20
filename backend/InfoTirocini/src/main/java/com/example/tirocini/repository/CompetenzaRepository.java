package com.example.tirocini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tirocini.model.Competenza;

@Repository
public interface CompetenzaRepository extends JpaRepository<Competenza, Integer> {

}