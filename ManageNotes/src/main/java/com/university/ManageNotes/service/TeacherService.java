package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


public interface TeacherService {

    TeacherRequest updateTeacher(Long teacherId, TeacherRequest request);

    TeacherResponse teacherProfile(Authentication authentication);

}