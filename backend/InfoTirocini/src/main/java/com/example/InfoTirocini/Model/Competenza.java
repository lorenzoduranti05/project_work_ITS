package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "competenza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competenza {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcompetenze")
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(name = "tipo_competenza", nullable = false)
    private String tipoCompetenza;
}