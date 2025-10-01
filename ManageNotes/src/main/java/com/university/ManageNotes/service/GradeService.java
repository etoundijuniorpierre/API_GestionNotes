package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Response.*;

import java.util.List;

public interface GradeService {

    GradeRequest createGrade(GradeRequest gradeRequest);
    
    GradeRequest updateGrade(Long gradeId, GradeRequest updateRequest);
    
    MessageResponse deleteGrade(Long gradeId);
    
    StudentResponse getStudentGrades(Long studentId, Long semesterId);

    List<GradeResponse> getTeacherGrades();
}