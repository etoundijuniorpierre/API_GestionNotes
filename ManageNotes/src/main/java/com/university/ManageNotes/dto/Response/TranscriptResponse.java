package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.enums.TranscriptStatus;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResponse {
    private Long transcriptId;
    private String studentFirstName;
    private String studentLastName;
    private String studentMatricule;
    private List<SubjectResponse> subjectResults;
    private TranscriptStatus status;
    private TeachingLevel studentLevel;
    private StudentCycle studentCycle;
    private String semesterName;
    private List<GradeResponse> studentGrades;
    private Double annualAverage;
    private String pdfPath;
    private Integer creditsEarned;
    
    // University promotion fields
    private Integer totalCreditsRequired; // 60 for academic year
    private Integer semester1Credits;
    private Integer semester2Credits;
    private Double semester1Average;
    private Double semester2Average;


    // new fields for header info
    private String facultyName;
    private String academicYear;

    private Instant createdDate;
    private Instant lastModifiedDate;
}
