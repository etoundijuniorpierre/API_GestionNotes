package com.university.ManageNotes.entities;



import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.Enum.SemestreList;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReleveDeNote extends BaseEntity {
    private Date dateGeneration;
    private Double moyenneSemestrielle;
    private Double moyenneGenerale;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @Enumerated(EnumType.STRING)
    private SemestreList semestre;

    @ManyToMany
    @JoinTable(
            name = "releve_note_details",
            joinColumns = @JoinColumn(name = "releve_de_note_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private List<Note> notesContenues = new ArrayList<>();
}
