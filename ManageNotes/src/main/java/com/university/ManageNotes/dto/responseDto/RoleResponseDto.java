package com.university.ManageNotes.dto.responseDto;

import com.university.ManageNotes.entities.Enum.RoleList;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class RoleResponseDto extends BaseResponseDto {
    private RoleList nomRole;
    private UtilisateurResponseDto utilisateur; // Nested DTO, consider a minimal DTO to avoid deep nesting/circularity
}
