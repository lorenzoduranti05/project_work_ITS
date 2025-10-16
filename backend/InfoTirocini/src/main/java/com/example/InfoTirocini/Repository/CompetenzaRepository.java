package com.example.InfoTirocini.Repository;

import com.example.InfoTirocini.Model.Competenza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenzaRepository extends JpaRepository<Competenza, Integer> {

}