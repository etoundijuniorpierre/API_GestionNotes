package com.university.ManageNotes.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkGradeRequest {
    @NotEmpty(message = "Grade list cannot be empty")
    @Valid
    private List<GradeRequest> grades;

    private Boolean validateDuplicates = true;
    private Boolean skipErrors = false;
}
