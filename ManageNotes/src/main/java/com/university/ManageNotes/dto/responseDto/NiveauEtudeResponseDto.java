package com.university.ManageNotes.dto.responseDto;


import com.university.ManageNotes.entities.Enum.NiveauEtudeList;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class NiveauEtudeResponseDto extends BaseResponseDto {
    private NiveauEtudeList nomNiveau;
    private String anneeScolaire;

}
