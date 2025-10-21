package com.example.tirocini.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "competenza")
public class Competenza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String tipoCompetenza;

    @ManyToMany(mappedBy = "competenze")
    @JsonIgnoreProperties("competenze")
    private List<Candidatura> candidature;

    @ManyToMany(mappedBy = "competenze")
    @JsonIgnoreProperties("competenze")
    private List<Lavoro> lavori;
 
    public Competenza() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoCompetenza() {
        return tipoCompetenza;
    }

    public void setTipoCompetenza(String tipoCompetenza) {
        this.tipoCompetenza = tipoCompetenza;
    }

    public List<Candidatura> getCandidature() {
        return candidature;
    }

    public void setCandidature(List<Candidatura> candidature) {
        this.candidature = candidature;
    }
    
    public List<Lavoro> getLavori() {
        return lavori;
    }

    public void setLavori(List<Lavoro> lavori) {
        this.lavori = lavori;
    }
}