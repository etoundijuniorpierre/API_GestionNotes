package com.university.ManageNotes.dto.responseDto;


import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
public class EmailNotificationResponseDto extends BaseResponseDto {
    private String sujet;
    private String corps;
    private Date dateEnvoi;
    private String statutEnvoi;
    private UtilisateurResponseDto destinataire; // This could be a simpler DTO to avoid deep nesting
}
