package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RevendicationRequest {
    @NotNull(message = "Period is required")
    private Exam period;
    
    @NotNull(message = "Student is required")
    private Student student;
    
    @NotNull(message = "Grade is required")
    private Grades grade;
    
    @NotNull(message = "Semester is required")
    private Semester semester;
    
    @NotNull(message = "Requested score is required")
    @DecimalMin(value = "0.0", message = "Requested score must be positive")
    @DecimalMax(value = "20.0", message = "Requested score cannot exceed 20")
    private Double requestedScore;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
}
