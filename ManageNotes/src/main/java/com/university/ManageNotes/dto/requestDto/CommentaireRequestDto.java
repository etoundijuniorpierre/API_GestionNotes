package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentaireRequestDto extends BaseResponseDto {
    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    private String contenu;

    @NotNull(message = "L'ID de la note associée est obligatoire")
    private Long noteId;

    @NotNull(message = "L'ID de l'enseignant est obligatoire")
    private Long enseignantId;
}