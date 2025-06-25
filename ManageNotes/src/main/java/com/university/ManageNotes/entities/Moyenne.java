package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.Enum.SemestreList;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
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
public class Moyenne extends BaseEntity {
    private Double valeurMoyenne;
    private Date dateCalcul;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @Enumerated(EnumType.STRING)
    private SemestreList semestre;

    // If you need average per subject as well, you'd add a ManyToOne Matiere
    // @ManyToOne
    // @JoinColumn(name = "matiere_id")
    // private Matiere matiere;
}