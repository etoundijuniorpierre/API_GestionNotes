package com.university.ManageNotes.dto.responseDto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MatiereResponseDto extends BaseResponseDto {
    private String nomMatiere;
    private String codeMatiere;
    private Double coefficientParDefaut;
    private NiveauEtudeResponseDto niveauEtude; // Nested DTO for response
}
