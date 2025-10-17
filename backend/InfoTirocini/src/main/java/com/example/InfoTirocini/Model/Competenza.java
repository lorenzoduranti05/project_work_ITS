package com.example.InfoTirocini.Model;

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
    private String tipoCompetenza; // "Tecnica", "Soft Skill", "Linguaggio", etc.
    
    // Lavori che richiedono questa competenza
    @ManyToMany(mappedBy = "competenze")
    @JsonIgnoreProperties("competenze")
    private List<Candidatura> candidature;
    
    // Costruttori
    public Competenza() {}
    
    // Getter e Setter
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
}