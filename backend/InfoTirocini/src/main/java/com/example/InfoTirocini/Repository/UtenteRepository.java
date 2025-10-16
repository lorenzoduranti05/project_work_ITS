package com.example.InfoTirocini.Repository;

import com.example.InfoTirocini.Model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Utente findByMail(String mail);
    
    boolean existsByMail(String mail);
}