package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.StudentCycle;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    @NotBlank(message = "Subject name is required")
    @Size(min = 3, max = 100, message = "Subject name must be between 3 and 100 characters")
    private String subjectName;
    
    @NotBlank(message = "Subject code is required")
    @Size(min = 3, max = 20, message = "Subject code must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Subject code must contain only uppercase letters and numbers")
    private String subjectCode;
    
    @NotNull(message = "Credits are required")
    @DecimalMin(value = "1", message = "Credits must be at least 1")
    @DecimalMax(value = "10", message = "Credits must not exceed 10")
    private BigDecimal credits;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
    
    private Long teacherId;
    
    @NotEmpty(message = "At least one teaching level is required")
    private List<TeachingLevel> subjectsLevel;
    
    @NotNull(message = "Student cycle is required")
    private StudentCycle Studentcycle;
    
    @NotNull(message = "Semester ID is required")
    private Long semesterId;
    
    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
