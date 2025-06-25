package com.university.ManageNotes.dto.responseDto;

import com.university.ManageNotes.entities.Enum.SemestreList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReleveDeNoteResponseDto extends BaseResponseDto {
    private Date dateGeneration;
    private Double moyenneSemestrielle;
    private Double moyenneGenerale;
    private EtudiantResponseDto etudiant;
    private SemestreList semestre;
    private List<NoteResponseDto> notesContenues; // Nested DTOs, be mindful of recursion
}
