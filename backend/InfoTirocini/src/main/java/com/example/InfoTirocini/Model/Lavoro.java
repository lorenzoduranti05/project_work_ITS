package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lavoro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lavoro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlavoro")
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String durata;
    
    @Column(nullable = false)
    private String orari;
    
    @Column(columnDefinition = "TEXT")
    private String descrizione;
    
    @ManyToOne
    @JoinColumn(name = "azienda_id")
    private Azienda azienda;
    
    @OneToMany(mappedBy = "lavoro")
    @JsonIgnore
    private List<Candidatura> candidature = new ArrayList<>();
}