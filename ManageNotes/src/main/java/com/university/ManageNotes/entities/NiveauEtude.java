package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.Enum.NiveauEtudeList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class NiveauEtude extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NiveauEtudeList nomNiveau;

    @Column(nullable = false)
    private String anneeScolaire; // e.g., "2023-2024"

    @OneToMany(mappedBy = "niveauEtude")
    private List<Etudiant> etudiants = new ArrayList<>();

    @ManyToMany(mappedBy = "niveauxAssignes")
    private List<Enseignant> enseignantsAssignes = new ArrayList<>();

    @OneToMany(mappedBy = "niveauEtude") // Matières contenues dans ce niveau
    private List<Matiere> matieres = new ArrayList<>();
}