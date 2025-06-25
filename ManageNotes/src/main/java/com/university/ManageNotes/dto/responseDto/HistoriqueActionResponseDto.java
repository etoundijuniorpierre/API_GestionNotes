package com.university.ManageNotes.dto.responseDto;


import com.university.ManageNotes.entities.Enum.ActionType;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@EqualsAndHashCode(callSuper = true)
public class HistoriqueActionResponseDto extends BaseResponseDto {
    private String description;
    private ActionType actionType;
    private UtilisateurResponseDto utilisateur;
    private Long targetEntityId;
    private String targetEntityType;

}
