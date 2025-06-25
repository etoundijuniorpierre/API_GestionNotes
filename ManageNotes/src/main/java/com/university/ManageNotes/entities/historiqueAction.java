package com.university.ManageNotes.entities;



import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import com.university.ManageNotes.entities.Enum.ActionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class historiqueAction extends BaseEntity {

    @Column(nullable = false, length = 500) // Increased length for description
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false) // The user who performed the action
    private Utilisateur utilisateur;


}
