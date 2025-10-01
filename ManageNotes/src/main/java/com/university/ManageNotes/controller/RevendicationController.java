package com.university.ManageNotes.controller;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.RevendicationResponse;
import com.university.ManageNotes.service.RevendicationService;
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
@Tag(name = "Revendication Management", description = "Grade revendication operations")
public class RevendicationController {
    private final RevendicationService revendicationService;

    @PostMapping("/student/revendication")
    @Operation(summary = "Create grade revendication (Student)", description = "Student submits a grade revendication request")
    public ResponseEntity<RevendicationRequest> createRevendication(@Valid @RequestBody RevendicationRequest request) {
        return new ResponseEntity<>(revendicationService.createRevendication(request), HttpStatus.CREATED);
    }

    @GetMapping("/teacher/revendications")
    @Operation(summary = "Get pending revendications (Teacher)", description = "Teacher views pending revendications for their subjects")
    public ResponseEntity<RevendicationResponse> getRevendicationsForTeacher(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_REVENDICATION_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        return new ResponseEntity<>(revendicationService.getRevendicationForTeacher(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/teacher/revendication/{id}/approve")
    @Operation(summary = "Approve revendication (Teacher)", description = "Teacher approves a grade revendication")
    public ResponseEntity<MessageResponse> approveRevendication(
            @PathVariable Long id,
            @RequestParam(required = false) String comment) {
        return new ResponseEntity<>(revendicationService.approveRevendication(id, comment), HttpStatus.OK);
    }

    @PostMapping("/teacher/revendication/{id}/reject")
    @Operation(summary = "Reject revendication (Teacher)", description = "Teacher rejects a grade revendication")
    public ResponseEntity<MessageResponse> rejectRevendication(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        return new ResponseEntity<>(revendicationService.rejectRevendication(id, reason), HttpStatus.OK);
    }
}