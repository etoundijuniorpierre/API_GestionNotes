package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.ReportRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.ReportResponse;
import com.university.ManageNotes.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> generateStudentReport(@PathVariable Long studentId,
                                                   @Valid @RequestBody ReportRequest reportRequest) {
        try {
            ReportResponse report = reportService.generateStudentReport(studentId, reportRequest);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to generate student report: " + e.getMessage()));
        }
    }

    @PostMapping("/class/{classId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> generateClassReport(@PathVariable Long classId,
                                                 @Valid @RequestBody ReportRequest reportRequest) {
        try {
            ReportResponse report = reportService.generateClassReport(classId, reportRequest);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to generate class report: " + e.getMessage()));
        }
    }

    @PostMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> generateSubjectReport(@PathVariable Long subjectId,
                                                   @Valid @RequestBody ReportRequest reportRequest) {
        try {
            ReportResponse report = reportService.generateSubjectReport(subjectId, reportRequest);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to generate subject report: " + e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}/pdf")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or (hasRole('STUDENT') and @securityService.isOwnStudent(#studentId))")
    public ResponseEntity<byte[]> downloadStudentReportPDF(@PathVariable Long studentId,
                                                           @RequestParam(required = false) String reportTitle) {
        try {
            // This would typically fetch student and grades from database
            // For now, using placeholder logic
            byte[] pdfBytes = reportService.generatePDFReport(null, null, reportTitle != null ? reportTitle : "Student Report");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "student_report_" + studentId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportGradesToExcel(@RequestParam(required = false) Long studentId,
                                                      @RequestParam(required = false) Long subjectId,
                                                      @RequestParam(required = false) Long semesterId) {
        try {
            // This would typically fetch grades based on filters
            byte[] excelBytes = reportService.exportToExcel(null); // Placeholder

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "grades_export.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGradeAnalytics(@RequestParam(required = false) Long semesterId,
                                               @RequestParam(required = false) Long subjectId) {
        try {
            // Analytics logic here
            return ResponseEntity.ok(MessageResponse.success("Analytics data retrieved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to retrieve analytics: " + e.getMessage()));
        }
    }
}
