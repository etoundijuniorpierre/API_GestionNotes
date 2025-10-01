package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.enums.AssessmentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GradeRequest {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Subject ID is required")
    private Long subjectId;
    
    @NotNull(message = "Exam ID is required")
    private Long examId;
    
    @NotNull(message = "Semester ID is required")
    private Long semesterId;
    
    @Min(value = 0, message = "CC score must be greater than or equal to 0")
    @Max(value = 30, message = "CC score must be less than or equal to 30")
    private Double ccScore;
    
    @Min(value = 0, message = "SN score must be greater than or equal to 0")
    @Max(value = 70, message = "SN score must be less than or equal to 70")
    private Double snScore;
    
    @Size(min = 5, max = 255, message = "Comments must be between 5 and 255 characters long")
    private String comments;
    
    @NotNull(message = "Assessment type is required")
    private AssessmentType assessmentType;
}
