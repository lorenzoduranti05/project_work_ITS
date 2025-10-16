package com.example.InfoTirocini.Repository;

import com.example.InfoTirocini.Model.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Integer> {
 
}