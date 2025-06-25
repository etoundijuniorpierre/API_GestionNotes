package com.university.ManageNotes.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UtilisateurResponseDto extends BaseResponseDto {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    // Do NOT include motDePasse in a response DTO for security!
}
