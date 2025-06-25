package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.Enum.TypeStatistique;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;



@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Statistique extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private TypeStatistique typeStatistique;


    @Lob // Can be used for long text fields
    @Column(columnDefinition = "TEXT")
    private String donneesStatistiques;


    private Date dateGeneration;

    @ManyToOne
    @JoinColumn(name = "niveau_etude_id")
    private NiveauEtude cibleNiveauEtude;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private Matiere cibleMatiere;
}

