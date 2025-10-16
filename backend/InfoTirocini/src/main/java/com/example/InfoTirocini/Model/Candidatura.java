package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidatura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcandidatura")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
    
    @ManyToOne
    @JoinColumn(name = "lavoro_id", nullable = false)
    private Lavoro lavoro;
    
    @Column(name = "data_candidatura")
    private LocalDateTime dataCandidatura = LocalDateTime.now();
    
    @Column(nullable = false)
    private String stato = "IN_ATTESA";
}