package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
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
public class Note extends BaseEntity {
    private Double valeurNote;
    private Date dateSaisie;
    private Double coefficientApplique;
    private Double noteCalculee; // This could also be a transient field or calculated in service

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;

    @OneToOne(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private Commentaire commentaire;

    // A method to calculate note could be here, or in a service.
    public void calculerNoteCoefficientee() {
        if (valeurNote != null && coefficientApplique != null) {
            this.noteCalculee = valeurNote * coefficientApplique;
        }
    }
}
