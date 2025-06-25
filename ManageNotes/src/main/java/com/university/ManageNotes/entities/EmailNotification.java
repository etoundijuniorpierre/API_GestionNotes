package com.university.ManageNotes.entities;


import com.university.ManageNotes.entities.AbstractClasses.BaseEntity;
import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
public class EmailNotification extends BaseEntity {
    private String sujet;
    private String corps;
    private Date dateEnvoi;
    private String statutEnvoi; // e.g., "SENT", "FAILED", "PENDING"

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Utilisateur destinataire;
}
