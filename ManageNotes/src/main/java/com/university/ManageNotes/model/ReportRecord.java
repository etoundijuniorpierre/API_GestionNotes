package com.university.ManageNotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ReportRecord extends AbstractEntity {
    private Long studentId;
    private Long semesterId;
    private Long subjectId;
    private Long classId;

    @Column(length = 50)
    private String reportType;

    private Double gpa;
    private String status;

    private String pdfPath;
    private String downloadUrl;

    private Long generatedBy;
}
