package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    public List<SubjectResponse> getAllSubjects() {
        // Implementation to get all subjects
        return List.of(); // Placeholder
    }

    public SubjectResponse getSubjectById(Long subjectId) {
        // Implementation to get subject by ID
        SubjectResponse response = new SubjectResponse();
        response.setId(subjectId);
        response.setName("Mathematics");
        response.setCode("MATH101");
        return response;
    }

    public MessageResponse createSubject(SubjectRequest subjectRequest) {
        try {
            // Implementation to create subject
            return MessageResponse.success("Subject created successfully");
        } catch (Exception e) {
            return MessageResponse.error("Failed to create subject: " + e.getMessage());
        }
    }

    public MessageResponse updateSubject(Long subjectId, SubjectRequest subjectRequest) {
        try {
            // Implementation to update subject
            return MessageResponse.success("Subject updated successfully");
        } catch (Exception e) {
            return MessageResponse.error("Failed to update subject: " + e.getMessage());
        }
    }

    public MessageResponse deleteSubject(Long subjectId) {
        try {
            // Implementation to delete subject
            return MessageResponse.success("Subject deleted successfully");
        } catch (Exception e) {
            return MessageResponse.error("Failed to delete subject: " + e.getMessage());
        }
    }

    public List<SubjectResponse> getSubjectsByTeacher(Long teacherId) {
        // Implementation to get subjects by teacher
        return List.of(); // Placeholder
    }

    public List<SubjectResponse> searchSubjects(String searchTerm) {
        // Implementation to search subjects
        return List.of(); // Placeholder
    }

    private SubjectResponse convertToResponse(Subject subject) {
        SubjectResponse response = new SubjectResponse();
        response.setId(subject.getId());
        response.setName(subject.getName());
        response.setCode(subject.getCode());
        response.setCredits(subject.getCredits());
        response.setCoefficient(subject.getCoefficient());
        return response;
    }
}
