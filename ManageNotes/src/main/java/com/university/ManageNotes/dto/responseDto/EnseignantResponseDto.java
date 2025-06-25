package com.university.ManageNotes.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnseignantResponseDto extends UtilisateurResponseDto {
    private List<MatiereResponseDto> specialites; // Nested DTOs
    private List<NiveauEtudeResponseDto> niveauxAssignes; // Nested DTOs
}
