package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.SemesterResponse;

import java.util.List;

public interface SemesterService {

    List<SemesterResponse> getAllSemesters();
    
    SemesterResponse getSemesterById(Long id);
    
    SemesterRequest createSemester(SemesterRequest request);
    
    SemesterRequest updateSemester(Long id, SemesterRequest request);
    
    void deleteSemester(Long id);
}