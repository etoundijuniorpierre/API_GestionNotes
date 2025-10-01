package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse {
    private Long examPeriodId;
    private AssessmentType assessmentType;
}