package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idutente")
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String cognome;
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    @Column(nullable = false)
    @JsonIgnore 
    private String password;
    
    @Column(nullable = false)
    private String ruolo = "USER";
    
    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private List<Candidatura> candidature = new ArrayList<>();
}