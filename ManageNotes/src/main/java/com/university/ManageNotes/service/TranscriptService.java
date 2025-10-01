package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Response.TranscriptResponse;
import com.university.ManageNotes.model.TeachingLevel;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TranscriptService {
    TranscriptResponse getTranscriptStudent(Long studentId);
    
    TranscriptResponse getStudentTranscript(Authentication authentication);
    
    List<TranscriptResponse> getTranscriptByTeachingLevel(TeachingLevel level);
}
