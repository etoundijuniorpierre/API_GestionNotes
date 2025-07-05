package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, UserRepository userRepository, SemesterRepository semesterRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.semesterRepository = semesterRepository;
    }

    public MessageResponse addGrade(GradeRequest gradeRequest) {
        try {
            Grades grade = new Grades();
            grade.setStudent(studentRepository.findById(gradeRequest.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found")));
            grade.setSubject(subjectRepository.findById(gradeRequest.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found")));
            grade.setValue(gradeRequest.getValue());
            grade.setCoefficient(gradeRequest.getCoefficient());
            grade.setType(gradeRequest.getType());
            grade.setComments(gradeRequest.getComments());
            grade.setEnteredBy(userRepository.findById(gradeRequest.getEnteredBy()).orElseThrow(() -> new RuntimeException("User not found")));
            gradeRepository.save(grade);
            return MessageResponse.success("Grade added successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Failed to add grade: " + e.getMessage());
        }
    }

    public GradeResponse updateGrade(Long gradeId, GradeUpdateRequest gradeRequest) {
        Grades grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        if (gradeRequest.getValue() != null) {
            grade.setValue(gradeRequest.getValue());
        }
        if (gradeRequest.getCoefficient() != null) {
            grade.setCoefficient(gradeRequest.getCoefficient());
        }
        if (gradeRequest.getComments() != null) {
            grade.setComments(gradeRequest.getComments());
        }
        if (gradeRequest.getType() != null) {
            grade.setType(gradeRequest.getType());
        }

        Grades updatedGrade = gradeRepository.save(grade);
        return convertToResponse(updatedGrade);
    }

    public MessageResponse deleteGrade(Long gradeId) {
        try {
            // Delete grade logic here
            return MessageResponse.success("Grade deleted successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Failed to delete grade: " + e.getMessage());
        }
    }

    public List<GradeResponse> getGradesByStudent(Long studentId) {
        // Implementation here
        return List.of();
    }

    public List<GradeResponse> getGradesBySubject(Long subjectId) {
        // Implementation here
        return List.of();
    }

    private GradeResponse convertToResponse(Grades grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setStudentId(grade.getStudent().getId());
        response.setStudentName(grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName());

        response.setSubjectId(grade.getSubject().getId());
        response.setSubjectName(grade.getSubject().getName());
        response.setSubjectCode(grade.getSubject().getCode());

        if (grade.getSemesters() != null) {
            response.setSemesterId(grade.getSemesters().getId());
            response.setSemesterName(grade.getSemesters().getName());
        }

        response.setValue(grade.getValue());
        response.setCoefficient(grade.getCoefficient());
        response.setType(grade.getType());
        response.setComments(grade.getComments());

        response.setEnteredBy(grade.getEnteredBy().getId());
        response.setEnteredByName(grade.getEnteredBy().getFirstName() + " " + grade.getEnteredBy().getLastName());

        response.setCreatedDate(grade.getCreatedDate());
        response.setLastModifiedDate(grade.getLastModifiedDate());

        return response;
    }

    public GradeResponse createGrade(GradeRequest gradeRequest) {
        Grades grade = new Grades();
        grade.setStudent(studentRepository.findById(gradeRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found")));
        grade.setSubject(subjectRepository.findById(gradeRequest.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found")));
        grade.setSemesters(semesterRepository.findById(gradeRequest.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        grade.setValue(gradeRequest.getValue());
        grade.setCoefficient(gradeRequest.getCoefficient());
        grade.setType(gradeRequest.getType());
        grade.setComments(gradeRequest.getComments());
        grade.setEnteredBy(userRepository.findById(gradeRequest.getEnteredBy())
                .orElseThrow(() -> new RuntimeException("User not found")));

        Grades saved = gradeRepository.save(grade);
        return convertToResponse(saved);
    }

    public StudentGradesResponse getStudentGrades(Long studentId, Long semesterId) {
        List<Grades> grades;
        if (semesterId != null) {
            grades = gradeRepository.findByStudentIdAndSemesterId(studentId, semesterId);
        } else {
            grades = gradeRepository.findByStudentId(studentId);
        }

        List<GradeResponse> gradeResponses = grades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        StudentGradesResponse response = new StudentGradesResponse();
        response.setStudentId(studentId);
        var studentOpt = studentRepository.findById(studentId);
        studentOpt.ifPresent(s -> response.setStudentName(s.getFirstName() + " " + s.getLastName()));
        if (semesterId != null) {
            var semOpt = semesterRepository.findById(semesterId);
            semOpt.ifPresent(se -> {
                response.setSemesterId(se.getId());
                response.setSemesterName(se.getName());
            });
        }
        response.setGrades(gradeResponses);

        // simple GPA calculation
        if (!grades.isEmpty()) {
            double total = grades.stream().mapToDouble(g -> g.getValue() * g.getCoefficient()).sum();
            double coeffSum = grades.stream().mapToDouble(Grades::getCoefficient).sum();
            if (coeffSum > 0)
                response.setGpa(Math.round((total / coeffSum) * 100.0) / 100.0);
        }

        return response;
    }

    public List<GradeResponse> getTeacherGrades() {
        Long teacherId = null;
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof com.university.ManageNotes.security.UserPrincipal up) {
                teacherId = up.getId();
            }
        } catch (Exception ignored) {}

        if (teacherId == null) {
            return java.util.List.of();
        }

        List<Grades> grades = gradeRepository.findByEnteredById(teacherId);
        return grades.stream().map(this::convertToResponse).collect(java.util.stream.Collectors.toList());
    }

    public List<Students> getStudentsBySemester(Long semesterId) {
        List<Grades> grades = gradeRepository.findBySemesterId(semesterId);
        return grades.stream()
                .map(Grades::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }
}
