package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import com.university.ManageNotes.entities.Enum.NiveauEtudeList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class NiveauEtudeRequestDto extends BaseResponseDto { // BaseResponseDto allows sending ID for updates
    @NotNull(message = "Le nom du niveau d'étude est obligatoire")
    private NiveauEtudeList nomNiveau;

    @NotBlank(message = "L'année scolaire est obligatoire")
    private String anneeScolaire;
}