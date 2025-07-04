package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Semesters;
import com.university.ManageNotes.repository.SemesterRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
@Tag(name = "Semester Management", description = "Create and list semesters (Admin only)")
public class SemesterController {

    @Autowired
    private SemesterRepository semesterRepository;

    @GetMapping
    @Operation(summary = "List all semesters")
    public List<Semesters> listSemesters() {
        return semesterRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new semester", description = "Admin only")
    public ResponseEntity<?> createSemester(@Valid @RequestBody Semesters semester) {
        if (semesterRepository.existsByName(semester.getName())) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Semester name already exists"));
        }
        semester.setActive(Boolean.TRUE.equals(semester.getActive()));
        Semesters saved = semesterRepository.save(semester);
        return ResponseEntity.ok(saved);
    }
}
