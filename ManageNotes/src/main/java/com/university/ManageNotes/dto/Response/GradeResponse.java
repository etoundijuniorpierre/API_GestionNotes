package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.*;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.AssessmentType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GradeResponse {
    private Long gradeId;
    private Double score;
    private Double maxValue;
    private String comments;
    private StudentRequest student;
    private SubjectRequest subject;
    private Teacher examiner;
    private SemesterRequest semester;
    private AssessmentType exam;
    private List<RevendicationRequest> revendication;
    private Boolean hasPassed;
    private Double gpa;
    private GradeRequest content;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
