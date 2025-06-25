package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MatiereRequestDto extends BaseResponseDto { // Using BaseResponseDto for ID in requests if needed for update
    @NotBlank(message = "Le nom de la matière ne peut pas être vide")
    @Size(max = 100, message = "Le nom de la matière ne doit pas dépasser 100 caractères")
    private String nomMatiere;

    @NotBlank(message = "Le code de la matière ne peut pas être vide")
    @Size(max = 20, message = "Le code de la matière ne doit pas dépasser 20 caractères")
    private String codeMatiere;

    @NotNull(message = "Le coefficient par défaut est obligatoire")
    private Double coefficientParDefaut;

    private Long niveauEtudeId; // Optional: Link to a specific NiveauEtude
}
