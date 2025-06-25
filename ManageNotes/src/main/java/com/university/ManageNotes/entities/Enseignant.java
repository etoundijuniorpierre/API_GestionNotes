package com.university.ManageNotes.entities;

import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor(force = true)
public class Enseignant extends Utilisateur {
    @ManyToMany
    @JoinTable(
            name = "enseignant_matiere",
            joinColumns = @JoinColumn(name = "enseignant_id"),
            inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    private List<Matiere> specialites = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "enseignant_niveau_etude",
            joinColumns = @JoinColumn(name = "enseignant_id"),
            inverseJoinColumns = @JoinColumn(name = "niveau_etude_id")
    )
    private List<NiveauEtude> niveauxAssignes = new ArrayList<>();

    @OneToMany(mappedBy = "enseignant")
    private List<Note> notesSaisies = new ArrayList<>();

    @OneToMany(mappedBy = "enseignant")
    private List<Commentaire> commentairesEcrits = new ArrayList<>();

    // Methods like saisirNote(), gererCoefficient() etc. would be in service layer
}
