package com.university.ManageNotes.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class NoteResponseDto extends BaseResponseDto {
    private Double valeurNote;
    private Date dateSaisie;
    private Double coefficientApplique;
    private Double noteCalculee;
    private MatiereResponseDto matiere;
    private EtudiantResponseDto etudiant; // Could be a simpler DTO to avoid circular dependencies
    private EnseignantResponseDto enseignant; // Could be a simpler DTO
    private CommentaireResponseDto commentaire; // Nested DTO for optional comment
}
