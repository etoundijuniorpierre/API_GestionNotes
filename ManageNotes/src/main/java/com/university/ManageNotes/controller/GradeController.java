package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Response.*;

import com.university.ManageNotes.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Grade Management", description = "Grade management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping("/teacher/grade")
    @Operation(summary = "Create new grade", description = "Create a new grade entry (Teacher/Admin only)")
    public ResponseEntity<GradeRequest> createGrade(@Valid @RequestBody GradeRequest gradeRequest) {
        return new ResponseEntity<>(gradeService.createGrade(gradeRequest), HttpStatus.CREATED);
    }

    @PutMapping("/teacher/grade/{gradeId}")
    @Operation(summary = "Update grade", description = "Update an existing grade (Teacher/Admin only)")
    public ResponseEntity<GradeRequest> updateGrade(
            @PathVariable Long gradeId,
            @Valid @RequestBody GradeRequest updateRequest) {
        return new ResponseEntity<>(gradeService.updateGrade(gradeId, updateRequest), HttpStatus.OK);
    }

    @DeleteMapping("/teacher/grade/{gradeId}")
    @Operation(summary = "Delete grade", description = "Delete a grade entry Teacher only")
    public ResponseEntity<MessageResponse> deleteGrade(@PathVariable Long gradeId) {
        return new ResponseEntity<>(gradeService.deleteGrade(gradeId), HttpStatus.OK);
    }


}
