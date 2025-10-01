package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.service.RevendicationPeriodService;
import io.swagger.v3.oas.annotations.Operation;
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
public class RevendicationPeriodController {
    private final RevendicationPeriodService revendicationPeriodService;

    @GetMapping("/revendication-period")
    @Operation(summary = "Get all the periods of revendication", description = "This endpoint allows to retrieve all the periods of revendication")
    public ResponseEntity<List<RevendicationPeriodResponse>> getAllRevendicationPeriod() {
        return new ResponseEntity<>(revendicationPeriodService.getAllPeriod(), HttpStatus.OK);
    }

    @PostMapping("/admin/revendication-period")
    @Operation(summary = "Create a new revendication period", description = "This endpoint allows the admin to define the period through which revendications are evalued")
    public ResponseEntity<RevendicationPeriodRequest> createRevendicatioPeriod(@Valid @RequestBody RevendicationPeriodRequest request) {
        return new ResponseEntity<>(revendicationPeriodService.createPeriod(request), HttpStatus.CREATED);
    }

    @PutMapping("/admin/revendication-period/{id}")
    @Operation(summary = "Update a revendication period", description = "This endpoint allows the admin to update the period through which revendications are evaluated")
    public ResponseEntity<RevendicationPeriodRequest> updateRevendicationPeriod(@PathVariable Long id, @Valid @RequestBody RevendicationPeriodRequest request) {
        return new ResponseEntity<>(revendicationPeriodService.updatePeriod(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/admin/revendication-period/{id}")
    @Operation(summary = "Delete a revendication period", description = "This endpoint allows the admin to delete a period through which revendications are evaluated")
    public ResponseEntity<MessageResponse> deleteRevendicationPeriod(@PathVariable Long id) {
        revendicationPeriodService.deletePeriod(id);
        return new ResponseEntity<>(new MessageResponse("Revendication period deleted successfully"), HttpStatus.OK);
    }

    @GetMapping("/revendication-period/active")
    @Operation(summary = "Get all active revendication periods", description = "This endpoint allows to retrieve all the active periods of revendication")
    public ResponseEntity<List<RevendicationPeriodResponse>> getActiveRevendicationPeriod() {
        return new ResponseEntity<>(revendicationPeriodService.getActivePeriods(), HttpStatus.OK);
    }
}
