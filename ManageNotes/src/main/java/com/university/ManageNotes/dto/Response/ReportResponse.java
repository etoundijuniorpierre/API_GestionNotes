package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse extends AbstractEntity {

    private Long studentId;
    private String studentName;
    private Long semesterId;
    private String semesterName;
    private Double gpa;
    private String status; // "PASS", "FAIL", "INCOMPLETE"
    private String pdfPath;

    private Long generatedBy;
    private String generatedByName;
    private String downloadUrl;

    // Additional fields for different report types
    private String reportType;
    private Long classId;
    private String className;
    private Long subjectId;
    private String subjectName;
    private Boolean success;
    private String message;

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
