package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import com.university.ManageNotes.entities.Enum.SemestreList;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoyenneRequestDto extends BaseResponseDto { // BaseResponseDto for ID if updating
    @NotNull(message = "La valeur de la moyenne est obligatoire")
    private Double valeurMoyenne;

    // dateCalcul is typically server-generated, so it might be omitted or nullable in request

    @NotNull(message = "L'ID de l'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "Le semestre est obligatoire")
    private SemestreList semestre;
}