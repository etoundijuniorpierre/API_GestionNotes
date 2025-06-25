package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Commentaire extends BaseEntity {
    private String contenu;
    private Date dateCommentaire;

    @OneToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

    @ManyToOne
    @JoinColumn(name = "enseignant_id") // Enseignant who wrote the comment
    private Enseignant enseignant;
}
