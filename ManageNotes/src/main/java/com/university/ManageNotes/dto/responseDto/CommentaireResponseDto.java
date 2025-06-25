package com.university.ManageNotes.dto.responseDto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentaireResponseDto extends BaseResponseDto {
    private String contenu;
    private Date dateCommentaire;
    private NoteResponseDto note;
    private EnseignantResponseDto enseignant; // Nested DTO
}
