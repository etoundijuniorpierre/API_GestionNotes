package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
public class Matiere extends BaseEntity {
    @Column(nullable = false)
    private String nomMatiere;

    @Column(unique = true, nullable = false)
    private String codeMatiere;

    private Double coefficientParDefaut;

    @ManyToMany(mappedBy = "specialites")
    private List<Enseignant> enseignants = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "niveau_etude_id") // Link to NiveauEtude
    private NiveauEtude niveauEtude;
}
