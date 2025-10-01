package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;


public interface SubjectService {

    SubjectResponse getAllSubjects(Integer pageNumber,
                                   Integer pageSize,
                                   String sortBy, String sortOrder);

    SubjectRequest createSubject(SubjectRequest request);

    SubjectRequest updateSubject(Long id, SubjectRequest request);

    SubjectRequest deleteSubject(Long id);

    SubjectResponse getAllSubjectsByTeacher(Long teacherId,
                                            Integer pageNumber,
                                            Integer pageSize,
                                            String sortBy, String sortOrder);
}
