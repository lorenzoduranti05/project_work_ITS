package com.example.tirocini.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "lavoro")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lavoro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String durata;
    private String orari;
    private String descrizione;
    
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "azienda_id")
    @JsonBackReference("azienda-lavori")
    private Azienda azienda;

    @OneToMany(mappedBy = "lavoro", cascade = CascadeType.ALL)
    @JsonManagedReference("lavoro-candidature")
    private List<Candidatura> candidature;

    @ManyToMany
    @JoinTable(
        name = "lavoro_competenza", 
        joinColumns = @JoinColumn(name = "lavoro_id"),
        inverseJoinColumns = @JoinColumn(name = "competenza_id")
    )
    @JsonIgnoreProperties("lavori")
    private List<Competenza> competenze;

    public Lavoro() {}

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

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getOrari() {
        return orari;
    }

    public void setOrari(String orari) {
        this.orari = orari;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public List<Candidatura> getCandidature() {
        return candidature;
    }

    public void setCandidature(List<Candidatura> candidature) {
        this.candidature = candidature;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Competenza> getCompetenze() {
        return competenze;
    }

    public void setCompetenze(List<Competenza> competenze) {
        this.competenze = competenze;
    }
}