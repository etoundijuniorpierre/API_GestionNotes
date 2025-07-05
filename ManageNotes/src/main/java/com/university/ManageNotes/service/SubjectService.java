package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAllOrderByName().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public SubjectResponse getSubjectById(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return convertToResponse(subject);
    }

    public MessageResponse createSubject(SubjectRequest subjectRequest) {
        try {
            if (subjectRepository.existsByCode(subjectRequest.getCode())) {
                return MessageResponse.error("Subject code already exists");
            }
            Subject subject = new Subject();
            subject.setName(subjectRequest.getName());
            subject.setCode(subjectRequest.getCode());
            subject.setCredits(java.math.BigDecimal.valueOf(subjectRequest.getCredits()));
            subject.setCoefficient(java.math.BigDecimal.valueOf(subjectRequest.getCoefficient()));
            subject.setIdTeacher(subjectRequest.getTeacherId());
            subjectRepository.save(subject);
            return new com.university.ManageNotes.dto.Response.MessageResponse("Subject created successfully","SUCCESS",convertToResponse(subject));
        } catch (Exception e) {
            return MessageResponse.error("Failed to create subject: " + e.getMessage());
        }
    }

    public MessageResponse updateSubject(Long subjectId, SubjectRequest subjectRequest) {
        try {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            if (!subject.getCode().equals(subjectRequest.getCode()) && subjectRepository.existsByCode(subjectRequest.getCode())) {
                return MessageResponse.error("Subject code already exists");
            }
            subject.setName(subjectRequest.getName());
            subject.setCode(subjectRequest.getCode());
            subject.setCredits(java.math.BigDecimal.valueOf(subjectRequest.getCredits()));
            subject.setCoefficient(java.math.BigDecimal.valueOf(subjectRequest.getCoefficient()));
            subject.setIdTeacher(subjectRequest.getTeacherId());
            subjectRepository.save(subject);
            return new com.university.ManageNotes.dto.Response.MessageResponse("Subject updated successfully","SUCCESS",convertToResponse(subject));
        } catch (Exception e) {
            return MessageResponse.error("Failed to update subject: " + e.getMessage());
        }
    }

    public MessageResponse deleteSubject(Long subjectId) {
        try {
            if (!subjectRepository.existsById(subjectId)) {
                return MessageResponse.error("Subject not found");
            }
            subjectRepository.deleteById(subjectId);
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
