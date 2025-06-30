package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade Management", description = "Grade management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping
    @Operation(summary = "Create new grade", description = "Create a new grade entry (Teacher/Admin only)")
    public ResponseEntity<GradeResponse> createGrade(@Valid @RequestBody GradeRequest gradeRequest) {
        try {
            GradeResponse response = gradeService.createGrade(gradeRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{gradeId}")
    @Operation(summary = "Update grade", description = "Update an existing grade (Teacher/Admin only)")
    public ResponseEntity<GradeResponse> updateGrade(
            @PathVariable Long gradeId,
            @Valid @RequestBody GradeUpdateRequest updateRequest) {
        try {
            GradeResponse response = gradeService.updateGrade(gradeId, updateRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{gradeId}")
    @Operation(summary = "Delete grade", description = "Delete a grade entry (Teacher/Admin only)")
    public ResponseEntity<MessageResponse> deleteGrade(@PathVariable Long gradeId) {
        try {
            gradeService.deleteGrade(gradeId);
            return ResponseEntity.ok(new MessageResponse("Grade deleted successfully", "SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error deleting grade: " + e.getMessage(), "ERROR", null));
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student grades", description = "Get all grades for a specific student")
    public ResponseEntity<StudentGradesResponse> getStudentGrades(
            @PathVariable Long studentId,
            @RequestParam(required = false) Long semesterId) {
        try {
            StudentGradesResponse response = gradeService.getStudentGrades(studentId, semesterId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/teacher/my-grades")
    @Operation(summary = "Get teacher's grades", description = "Get all grades entered by current teacher")
    public ResponseEntity<List<GradeResponse>> getTeacherGrades() {
        try {
            List<GradeResponse> response = gradeService.getTeacherGrades();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
