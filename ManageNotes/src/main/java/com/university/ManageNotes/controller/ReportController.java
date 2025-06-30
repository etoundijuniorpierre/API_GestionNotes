package com.university.ManageNotes.controller;

import com.university.ManageNotes.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report Management", description = "PDF report generation endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/student/{studentId}/transcript")
    @Operation(summary = "Generate student transcript", description = "Generate PDF transcript for a student")
    public ResponseEntity<byte[]> generateStudentTranscript(
            @PathVariable Long studentId,
            @RequestParam(required = false) Long semesterId) {
        try {
            byte[] pdfBytes = reportService.generateStudentTranscript(studentId, semesterId);

            String filename = "student_transcript_" + studentId;
            if (semesterId != null) {
                filename += "_semester_" + semesterId;
            }
            filename += ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/subject/{subjectId}/grades")
    @Operation(summary = "Generate subject grades report", description = "Generate PDF report for all grades in a subject (Teacher only)")
    public ResponseEntity<byte[]> generateSubjectGradesReport(@PathVariable Long subjectId) {
        try {
            byte[] pdfBytes = reportService.generateSubjectGradesReport(subjectId);

            String filename = "subject_grades_" + subjectId + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
