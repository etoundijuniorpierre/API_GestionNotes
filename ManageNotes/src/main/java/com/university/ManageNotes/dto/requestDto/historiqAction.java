package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import com.university.ManageNotes.entities.Enum.ActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class historiqAction extends BaseEntity {

    @Column(nullable = false, length = 500) // Increased length for description
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false) // The user who performed the action
    private Utilisateur utilisateur;

    @Column(name = "target_entity_id")
    private Long targetEntityId; // ID of the entity that was acted upon (e.g., Note ID, Etudiant ID)

    @Column(name = "target_entity_type", length = 100)
    private String targetEntityType; // Type of the entity (e.g., "Note", "Etudiant", "Matiere")

    // You can add more fields if needed, e.g., IP address, old/new values (JSONB)
}
