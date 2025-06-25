package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailNotificationRequestDto extends BaseResponseDto {
    @NotBlank(message = "Le sujet de l'email est obligatoire")
    @Size(max = 255, message = "Le sujet ne doit pas dépasser 255 caractères")
    private String sujet;

    @NotBlank(message = "Le corps de l'email est obligatoire")
    private String corps;

    // dateEnvoi and statutEnvoi are typically server-generated

    @NotNull(message = "L'ID du destinataire est obligatoire")
    private Long destinataireId; // ID of the Utilisateur
}
