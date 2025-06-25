package com.university.ManageNotes.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class EtudiantResponseDto extends UtilisateurResponseDto {
    private Date dateNaissance;
    private NiveauEtudeResponseDto niveauEtude; // Include nested DTO for related entity
}