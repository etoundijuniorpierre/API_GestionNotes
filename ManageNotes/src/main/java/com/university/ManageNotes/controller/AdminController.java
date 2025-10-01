package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin-specific operations")
public class AdminController {

    private final TeacherService teacherService;

    @PutMapping("/admin/teacher/{id}")
    @Operation(summary = "Update Teacher information (Admin only)")
    public ResponseEntity<TeacherRequest> updateTeacher(
            @PathVariable Long id, 
            @Valid @RequestBody TeacherRequest request) {
        return new ResponseEntity<>(teacherService.updateTeacher(id, request), HttpStatus.OK);
    }
}