package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import com.university.ManageNotes.entities.Enum.TypeStatistique;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class StatistiqueRequestDto extends BaseResponseDto {
    @NotNull(message = "Le type de statistique est obligatoire")
    private TypeStatistique typeStatistique;

    // Data will usually be generated, not provided in a request for generation.
    // If for updating an existing statistic, you might include it:
    private String donneesStatistiquesJson; // As a JSON string

    private Long cibleNiveauEtudeId;
    private Long cibleMatiereId;
}

