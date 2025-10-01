package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    @NotBlank(message = "Department name is required")
    @Size(min = 5, max = 100, message = "Department name must be between 5 and 100 characters")
    private String departmentName;
    
    private Set<Long> subjectIds;
}