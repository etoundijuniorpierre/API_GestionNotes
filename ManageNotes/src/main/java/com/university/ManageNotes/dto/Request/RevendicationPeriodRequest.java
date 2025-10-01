package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RevendicationPeriodRequest {
    @NotNull(message = "Exam ID is required")
    private Long examId;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private String color;
    private Boolean isActive = false;
}
