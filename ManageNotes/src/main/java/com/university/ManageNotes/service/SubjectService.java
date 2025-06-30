package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasRole('ADMIN')")
    public Subject createSubject(Subject subject) {
        // Validate unique code
        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new RuntimeException("Subject code already exists");
        }

        // Validate teacher exists and has TEACHER role
        if (subject.getIdTeacher() != null) {
            Users teacher = userRepository.findById(subject.getIdTeacher())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            if (teacher.getRole() != Role.TEACHER) {
                throw new RuntimeException("Assigned user must have TEACHER role");
            }
        }

        return subjectRepository.save(subject);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Subject updateSubject(Long subjectId, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Update fields
        subject.setName(subjectDetails.getName());
        subject.setCredits(subjectDetails.getCredits());
        subject.setCoefficient(subjectDetails.getCoefficient());

        // Check code uniqueness if changed
        if (!subject.getCode().equals(subjectDetails.getCode())) {
            if (subjectRepository.existsByCode(subjectDetails.getCode())) {
                throw new RuntimeException("Subject code already exists");
            }
            subject.setCode(subjectDetails.getCode());
        }

        // Validate teacher if changed
        if (subjectDetails.getIdTeacher() != null &&
            !subjectDetails.getIdTeacher().equals(subject.getIdTeacher())) {
            Users teacher = userRepository.findById(subjectDetails.getIdTeacher())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            if (teacher.getRole() != Role.TEACHER) {
                throw new RuntimeException("Assigned user must have TEACHER role");
            }
            subject.setIdTeacher(subjectDetails.getIdTeacher());
        }

        return subjectRepository.save(subject);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subjectRepository.delete(subject);
    }

    public Optional<Subject> getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId);
    }

    public Optional<Subject> getSubjectByCode(String code) {
        return subjectRepository.findByCode(code);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAllOrderByName();
    }

    public List<Subject> searchSubjectsByName(String name) {
        return subjectRepository.findByNameContainingIgnoreCase(name);
    }

    @PreAuthorize("hasRole('TEACHER')")
    public List<Subject> getMySubjects() {
        Users currentUser = authService.getCurrentUser();
        return subjectRepository.findSubjectsByTeacherOrderByName(currentUser.getId());
    }

    public List<Subject> getSubjectsByTeacher(Long teacherId) {
        return subjectRepository.findSubjectsByTeacherOrderByName(teacherId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Subject assignTeacher(Long subjectId, Long teacherId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Users teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("User must have TEACHER role");
        }

        subject.setIdTeacher(teacherId);
        return subjectRepository.save(subject);
    }
}
