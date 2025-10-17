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
    
    @Column(nullable = false) //la colonna nel database non può contenere valori NULL.
    private String nome;
    
    @Column(nullable = false)
    private String cognome;
    
    @Column(nullable = false, unique = true) //ogni valore in questa colonna debba essere unico.
    private String mail;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String ruolo = "USER";
    
    @OneToMany(mappedBy = "utente")
    private List<Candidatura> candidature = new ArrayList<>();
}