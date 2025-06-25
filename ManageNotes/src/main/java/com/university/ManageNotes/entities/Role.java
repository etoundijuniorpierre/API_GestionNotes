package com.university.ManageNotes.entities;

import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import com.university.ManageNotes.entities.Enum.RoleList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@Entity
@Data
@EqualsAndHashCode(callSuper = true) // Important for entities extending BaseEntity
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private RoleList nomRole;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id") // Foreign key column
    private Utilisateur utilisateur;
}
