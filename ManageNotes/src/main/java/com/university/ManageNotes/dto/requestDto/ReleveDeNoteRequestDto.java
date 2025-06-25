package com.university.ManageNotes.dto.requestDto;


import com.university.ManageNotes.dto.responseDto.BaseResponseDto;
import com.university.ManageNotes.entities.Enum.SemestreList;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReleveDeNoteRequestDto extends BaseResponseDto {
    // For generation, you might only need studentId and semester
    // If updating, you might send average fields
    private Double moyenneSemestrielle;
    private Double moyenneGenerale;

    @NotNull(message = "L'ID de l'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "Le semestre est obligatoire")
    private SemestreList semestre;

    private List<Long> notesContenuesIds; // List of Note IDs
}
