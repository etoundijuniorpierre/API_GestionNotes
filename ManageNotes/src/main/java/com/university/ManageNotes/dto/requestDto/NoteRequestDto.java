package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
public class NoteRequestDto extends BaseResponseDto {
    @NotNull(message = "La valeur de la note est obligatoire")
    @Min(value = 0, message = "La note minimale est 0")
    @Max(value = 20, message = "La note maximale est 20")
    private Double valeurNote;

    @NotNull(message = "Le coefficient appliqué est obligatoire")
    private Double coefficientApplique;

    @NotNull(message = "L'ID de la matière est obligatoire")
    private Long matiereId;

    @NotNull(message = "L'ID de l'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "L'ID de l'enseignant est obligatoire")
    private Long enseignantId;
}
