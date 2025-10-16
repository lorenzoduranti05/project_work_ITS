package com.example.InfoTirocini.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "azienda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Azienda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idazienda")
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @OneToMany(mappedBy = "azienda")
    @JsonIgnore
    private List<Lavoro> lavori = new ArrayList<>();
}