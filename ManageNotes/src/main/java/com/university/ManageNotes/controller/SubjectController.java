package com.university.ManageNotes.controller;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.service.SubjectService;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Subject Lookup", description = "Endpoints for subject lookup")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/admin/subjects")
    @Operation(summary = "View all subject", description = "Admin can view all the subjects")
    public ResponseEntity<SubjectResponse> getAllSubject(
            @RequestParam(name = "pageNumber",
                    defaultValue = AppConstant.PAGE_NUMBER,
                    required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",
                    defaultValue = AppConstant.PAGE_SIZE,
                    required = false) Integer pageSize,
            @RequestParam(name = "sortBy",
                    defaultValue = AppConstant.SORT_SUBJECT_BY,
                    required = false) String sortBy,
            @RequestParam(name = "sortOrder",
                    defaultValue = AppConstant.SORT_DIR,
                    required = false) String sortOrder
    ) {
        return new ResponseEntity<>(subjectService.getAllSubjects
                (pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);
    }


    @PostMapping("/admin/subject")
    @Operation(summary = "Create subject", description = "Admin can create a subject")
    public ResponseEntity<SubjectRequest> createSubject(@Valid @RequestBody SubjectRequest request) {
        return new ResponseEntity<>(subjectService.createSubject(request), HttpStatus.CREATED) ;
    }

    @PutMapping("/admin/subject/{id}")
    @Operation(summary = "Update subject", description = "Admin can update a subject")
    public ResponseEntity<SubjectRequest> updateSubject(@PathVariable Long id,
                                  @Valid @RequestBody SubjectRequest request) {
        return new ResponseEntity<>(subjectService.updateSubject(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/admin/subject/{id}")
    @Operation(summary = "Delete subject",
            description = "Admin can delete a subject")
    public ResponseEntity<SubjectRequest> delete(@PathVariable Long id) {
        return new ResponseEntity<>(subjectService.deleteSubject(id), HttpStatus.OK) ;
    }

    @GetMapping("/teacher/subject")
    @Operation(summary = "Get subjects assigned to current teacher",
            description = "Return subjects taught by the authenticated teacher")
    public ResponseEntity<SubjectResponse> getSubjectsByTeacher(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                                                      @RequestParam(name = "pageNumber",
                                                              defaultValue = AppConstant.PAGE_NUMBER,
                                                              required = false) Integer pageNumber,
                                                      @RequestParam(name = "pageSize",
                                                              defaultValue = AppConstant.PAGE_SIZE,
                                                              required = false) Integer pageSize,
                                                      @RequestParam(name = "sortBy",
                                                              defaultValue = AppConstant.SORT_SUBJECT_BY,
                                                              required = false) String sortBy,
                                                      @RequestParam(name = "sortOrder",
                                                              defaultValue = AppConstant.SORT_DIR,
                                                              required = false) String sortOrder
    ) {
        Long teacherId = userPrincipal.getId();
        return new ResponseEntity<>(subjectService.getAllSubjectsByTeacher
                (teacherId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.CREATED);
    }
}
