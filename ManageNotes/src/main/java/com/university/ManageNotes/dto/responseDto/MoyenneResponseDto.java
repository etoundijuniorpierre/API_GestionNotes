package com.university.ManageNotes.dto.responseDto;


import com.university.ManageNotes.entities.Enum.SemestreList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoyenneResponseDto extends BaseResponseDto {
    private Double valeurMoyenne;
    private Date dateCalcul;
    private EtudiantResponseDto etudiant; // Nested DTO, consider a simpler EtudiantIdNameDto if full obj is too much
    private SemestreList semestre;
}