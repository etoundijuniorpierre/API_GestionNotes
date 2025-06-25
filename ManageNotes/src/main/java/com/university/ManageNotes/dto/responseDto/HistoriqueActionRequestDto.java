package com.university.ManageNotes.dto.responseDto;


import com.university.ManageNotes.entities.Enum.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoriqueActionRequestDto extends BaseResponseDto {
    @NotBlank(message = "La description de l'action ne peut pas être vide")
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @NotNull(message = "Le type d'action est obligatoire")
    private ActionType actionType;

    @NotNull(message = "L'ID de l'utilisateur effectuant l'action est obligatoire")
    private Long utilisateurId;


}
