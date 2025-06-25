package com.university.ManageNotes.entities;

import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@SuperBuilder
@NoArgsConstructor(force = true)
public class Etudiant extends Utilisateur {
    private Date dateNaissance;

    @ManyToOne
    @JoinColumn(name = "niveau_etude_id", nullable = false)
    private NiveauEtude niveauEtude;

    @OneToMany(mappedBy = "etudiant")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant")
    private List<Moyenne> moyennes = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant")
    private List<ReleveDeNote> relevesDeNote = new ArrayList<>();
}