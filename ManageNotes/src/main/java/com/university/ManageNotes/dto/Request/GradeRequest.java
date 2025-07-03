package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.GradeType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

    @NotNull(message = "Semester ID is required")
    private Long semesterId;

    @NotNull(message = "Grade value is required")
    @DecimalMin(value = "0.0", message = "Grade value must be at least 0")
    @DecimalMax(value = "20.0", message = "Grade value must not exceed 20")
    private Double value;

    @DecimalMin(value = "0.1", message = "Coefficient must be at least 0.1")
    @DecimalMax(value = "5.0", message = "Coefficient must not exceed 5.0")
    private Double coefficient = 1.0;

    @NotNull(message = "Grade type is required")
    private GradeType type;

    @NotNull(message = "Entered by user ID is required")
    private Long enteredBy;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments;

}
