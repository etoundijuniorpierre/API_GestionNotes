package com.university.ManageNotes.dto.requestDto;

import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnseignantRequestDto extends UtilisateurRequestDto {
    private List<Long> specialitesIds;
    private List<Long> niveauxAssignesIds;
}
