package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatura")
public class Candidatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "utente_id")
    @JsonBackReference("utente-candidature")
    private Utente utente;
    
    @ManyToOne
    @JoinColumn(name = "lavoro_id")
    @JsonBackReference("lavoro-candidature")
    private Lavoro lavoro;
    
    private LocalDateTime dataCandidatura;
    private String stato;
    
    // Costruttori
    public Candidatura() {}
    
    // Getter e Setter
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Utente getUtente() {
        return utente;
    }
    
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
    public Lavoro getLavoro() {
        return lavoro;
    }
    
    public void setLavoro(Lavoro lavoro) {
        this.lavoro = lavoro;
    }
    
    public LocalDateTime getDataCandidatura() {
        return dataCandidatura;
    }
    
    public void setDataCandidatura(LocalDateTime dataCandidatura) {
        this.dataCandidatura = dataCandidatura;
    }
    
    public String getStato() {
        return stato;
    }
    
    public void setStato(String stato) {
        this.stato = stato;
    }
}