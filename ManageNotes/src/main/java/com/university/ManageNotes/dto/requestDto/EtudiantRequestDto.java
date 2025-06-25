package com.university.ManageNotes.dto.requestDto;

import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class EtudiantRequestDto extends UtilisateurRequestDto {
    @NotNull(message = "La date de naissance est obligatoire")
    private Date dateNaissance;

    @NotNull(message = "Le niveau d'étude est obligatoire")
    private Long niveauEtudeId; // We send/receive only the ID for relationships
}