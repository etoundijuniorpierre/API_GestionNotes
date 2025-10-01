package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.TranscriptResponse;
import com.university.ManageNotes.service.TranscriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Transcript Management", description = "Student transcript operations")
public class TranscriptController {

    private final TranscriptService transcriptService;

    @GetMapping("/student/transcript")
    @Operation(summary = "Get student transcript", description = "Student retrieves their own academic transcript")
    public ResponseEntity<TranscriptResponse> getStudentTranscript(Authentication authentication) {
        return new ResponseEntity<>(transcriptService.getStudentTranscript(authentication), HttpStatus.OK);
    }
}