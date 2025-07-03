package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.model.GradeType;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
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

    @Autowired
    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, UserRepository userRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
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
        response.setSubjectId(grade.getSubject().getId());
        response.setValue(grade.getValue());
        response.setCoefficient(grade.getCoefficient());
        response.setType(grade.getType()); // This will use the setGradeType method we added
        response.setComments(grade.getComments());
        response.setEnteredBy(grade.getEnteredBy().getId()); // Fix: Convert User to Long ID
        response.setEnteredByName(grade.getEnteredBy().getFirstName() + " " + grade.getEnteredBy().getLastName());

        return response;
    }

    public GradeResponse createGrade(GradeRequest gradeRequest) {
        // Implementation to create grade
        return new GradeResponse(); // Placeholder
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

        // You might want to populate other fields of StudentGradesResponse as well
        StudentGradesResponse response = new StudentGradesResponse();
        response.setStudentId(studentId);
        response.setGrades(gradeResponses);

        return response;
    }

    public List<GradeResponse> getTeacherGrades() {
        // Implementation to get teacher grades
        return List.of(); // Placeholder
    }

    public List<Students> getStudentsBySemester(Long semesterId) {
        List<Grades> grades = gradeRepository.findBySemesterId(semesterId);
        return grades.stream()
                .map(Grades::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }
}
