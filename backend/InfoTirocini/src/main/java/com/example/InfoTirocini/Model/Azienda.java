package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "azienda")
public class Azienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;
    
    @OneToMany(mappedBy = "azienda", cascade = CascadeType.ALL)
    @JsonManagedReference("azienda-lavori")
    private List<Lavoro> lavori;
    
    // Costruttori
    public Azienda() {}
    
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
    
    public List<Lavoro> getLavori() {
        return lavori;
    }
    
    public void setLavori(List<Lavoro> lavori) {
        this.lavori = lavori;
    }
}