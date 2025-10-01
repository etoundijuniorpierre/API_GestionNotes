package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SemesterResponse;
import com.university.ManageNotes.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Semester Management", description = "CRUD operations for semesters")
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping("/semesters")
    @Operation(summary = "Get all semesters", description = "Retrieve a list of all semesters")
    public ResponseEntity<List<SemesterResponse>> getAllSemesters() {
        return new ResponseEntity<>(semesterService.getAllSemesters(), HttpStatus.OK);
    }

    @PostMapping("/admin/semester")
    @Operation(summary = "Create new semester", description = "Create a new semester with the provided details")
    public ResponseEntity<SemesterRequest> createSemester(@Valid @RequestBody SemesterRequest request) {
        return new ResponseEntity<>(semesterService.createSemester(request), HttpStatus.CREATED);
    }

    @PutMapping("/admin/semester/{id}")
    @Operation(summary = "Update semester", description = "Update the details of a semester with the provided ID")
    public ResponseEntity<SemesterRequest> updateSemester(@PathVariable Long id, @Valid @RequestBody SemesterRequest request) {
        return new ResponseEntity<>(semesterService.updateSemester(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/admin/semester/{id}")
    @Operation(summary = "Delete semester", description = "Delete the semester with the provided ID")
    public ResponseEntity<MessageResponse> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return new ResponseEntity<>(new MessageResponse("Semester deleted successfully"), HttpStatus.OK);
    }
}