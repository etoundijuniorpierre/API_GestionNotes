package com.university.ManageNotes.dto.responseDto;



import com.university.ManageNotes.entities.Enum.TypeStatistique;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
public class StatistiqueResponseDto extends BaseResponseDto {
    private TypeStatistique typeStatistique;
    private String donneesStatistiquesJson; // Or Map<String, Object> if converted in entity
    private Date dateGeneration;
    private NiveauEtudeResponseDto cibleNiveauEtude;
    private MatiereResponseDto cibleMatiere;
}

