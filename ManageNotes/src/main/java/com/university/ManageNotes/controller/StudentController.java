package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Response.RevendicationResponse;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.service.GradeService;
import com.university.ManageNotes.service.RevendicationService;
import com.university.ManageNotes.service.StudentService;
import com.university.ManageNotes.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin-specific operations")
public class StudentController {

    private final StudentService studentService;
    private final GradeService gradeService;
    private final RevendicationService revendicationService;

    @PutMapping("/admin/student/{id}")
    @Operation(summary = "Update Student information (Admin only)")
    public ResponseEntity<StudentRequest> updateStudent(
            @PathVariable Long id, 
            @Valid @RequestBody StudentRequest request) {
        return new ResponseEntity<>(studentService.updateStudent(id, request), HttpStatus.OK);
    }

    @GetMapping("/student/profile")
    @Operation(summary = "Get current Student profile", description = "This endpoint provide the informations of the current logged in Student")
    public ResponseEntity<StudentResponse> getStudentDetails(Authentication authentication) {
        StudentResponse studentProfileReponse = studentService.studentProfile(authentication);
        return new ResponseEntity<>(studentProfileReponse, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student grades", description = "Get all grades for a specific student")
    public ResponseEntity<StudentResponse> getStudentGrades(
            @PathVariable Long studentId,
            @RequestParam(required = false) Long semesterId) {
        return new ResponseEntity<>(gradeService.getStudentGrades(studentId, semesterId), HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}/revendications")
    @Operation(summary = "Get student revendications (Student)", description = "Student views their revendication status and comments")
    public ResponseEntity<List<RevendicationResponse>> getStudentRevendications(@PathVariable Long studentId) {
        return new ResponseEntity<>(revendicationService.getStudentRevendications(studentId), HttpStatus.OK);
    }
}