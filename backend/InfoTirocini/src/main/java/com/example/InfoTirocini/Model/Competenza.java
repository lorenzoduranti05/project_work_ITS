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
    private String tipoCompetenza;
    
    @ManyToMany(mappedBy = "competenze")
    @JsonIgnoreProperties({"competenze", "candidature"})
    private List<Utente> utenti;
    
    @ManyToMany(mappedBy = "competenze")
    @JsonIgnoreProperties({"competenze", "candidature", "azienda"})
    private List<Lavoro> lavori;
    
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
    
    public List<Utente> getUtenti() {
        return utenti;
    }
    
    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
    }
    
    public List<Lavoro> getLavori() {
        return lavori;
    }
    
    public void setLavori(List<Lavoro> lavori) {
        this.lavori = lavori;
    }
}