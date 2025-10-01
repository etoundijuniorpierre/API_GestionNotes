package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Semester;
import com.university.ManageNotes.model.Student;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class TranscriptRequest {
    @NotNull(message = "Student is required")
    private Student student;
    
    @NotNull(message = "Semester is required")
    private Semester semester;
    
    @Pattern(regexp = "^(PDF|pdf)$", message = "Format must be PDF")
    private String format = "PDF";
    
    private Boolean includeComments = true;

    @NotBlank(message = "Faculty name is required")
    @Size(min = 3, max = 100, message = "Faculty name must be between 3 and 100 characters")
    private String facultyName;
    
    @NotBlank(message = "Academic year is required")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Academic year must be in format YYYY-YYYY (e.g., 2024-2025)")
    private String academicYear;
}
