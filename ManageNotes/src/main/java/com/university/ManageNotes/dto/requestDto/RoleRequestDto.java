package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import com.university.ManageNotes.entities.Enum.RoleList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@EqualsAndHashCode(callSuper = true)
public class RoleRequestDto extends BaseResponseDto {
    @NotNull(message = "Le nom du rôle est obligatoire")
    private RoleList nomRole;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;
}
