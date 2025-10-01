package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface StudentService {
    @Transactional
    StudentRequest updateStudent(Long studentId, StudentRequest request);

    StudentResponse studentProfile(Authentication authentication);
}
