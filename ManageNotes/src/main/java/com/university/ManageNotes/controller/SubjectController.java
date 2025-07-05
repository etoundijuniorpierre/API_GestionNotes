package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Subject Lookup", description = "Endpoints for subject lookup")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    @Operation(summary = "Get all subjects")
    public List<SubjectResponse> all() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by id")
    public SubjectResponse one(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Create subject", description = "Teacher or Admin can create a subject")
    public MessageResponse create(@Valid @RequestBody SubjectRequest request) {
        return subjectService.createSubject(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Update subject", description = "Teacher or Admin can update a subject")
    public MessageResponse update(@PathVariable Long id, @Valid @RequestBody SubjectRequest request) {
        return subjectService.updateSubject(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Delete subject", description = "Teacher or Admin can delete a subject")
    public MessageResponse delete(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }
}
