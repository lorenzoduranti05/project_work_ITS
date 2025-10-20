package com.example.tirocini.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

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
    
    @ManyToMany
    @JoinTable(
        name = "candidatura_competenza",
        joinColumns = @JoinColumn(name = "candidatura_id"),
        inverseJoinColumns = @JoinColumn(name = "competenza_id")
    )
    @JsonIgnoreProperties("candidature")
    private List<Competenza> competenze;


    public Candidatura() {}


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

    public List<Competenza> getCompetenze() {
        return competenze;
    }

    public void setCompetenze(List<Competenza> competenze) {
        this.competenze = competenze;
    }
}