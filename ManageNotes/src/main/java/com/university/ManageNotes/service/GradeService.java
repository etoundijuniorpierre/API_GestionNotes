package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public GradeResponse createGrade(GradeRequest gradeRequest) {
        // Validate entities exist
        Students student = studentRepository.findById(gradeRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(gradeRequest.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Semesters semester = semesterRepository.findById(gradeRequest.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        Users currentUser = authService.getCurrentUser();

        // For teachers, verify they can only grade their subjects
        if (currentUser.getRole() == Role.TEACHER && !subject.getIdTeacher().equals(currentUser.getId())) {
            throw new RuntimeException("Teachers can only grade their assigned subjects");
        }

        // Create new grade
        Grades grade = new Grades();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSemesters(semester);
        grade.setValue(gradeRequest.getValue());
        grade.setCoefficient(gradeRequest.getCoefficient());
        grade.setGradeType(gradeRequest.getType());
        grade.setComments(gradeRequest.getComments());
        grade.setEnteredBy(currentUser);

        grade = gradeRepository.save(grade);

        return convertToGradeResponse(grade);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public GradeResponse updateGrade(Long gradeId, GradeUpdateRequest updateRequest) {
        Grades grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        Users currentUser = authService.getCurrentUser();

        // Verify permissions
        if (currentUser.getRole() == Role.TEACHER &&
            !grade.getSubject().getIdTeacher().equals(currentUser.getId())) {
            throw new RuntimeException("Teachers can only update grades for their subjects");
        }

        // Update fields
        if (updateRequest.getValue() != null) {
            grade.setValue(updateRequest.getValue());
        }
        if (updateRequest.getCoefficient() != null) {
            grade.setCoefficient(updateRequest.getCoefficient());
        }
        if (updateRequest.getComments() != null) {
            grade.setComments(updateRequest.getComments());
        }

        grade = gradeRepository.save(grade);
        return convertToGradeResponse(grade);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public void deleteGrade(Long gradeId) {
        Grades grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        Users currentUser = authService.getCurrentUser();

        // Verify permissions
        if (currentUser.getRole() == Role.TEACHER &&
            !grade.getSubject().getIdTeacher().equals(currentUser.getId())) {
            throw new RuntimeException("Teachers can only delete grades for their subjects");
        }

        gradeRepository.delete(grade);
    }

    public StudentGradesResponse getStudentGrades(Long studentId, Long semesterId) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Grades> grades;
        if (semesterId != null) {
            Semesters semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new RuntimeException("Semester not found"));
            grades = gradeRepository.findByStudentAndSemesters(student, semester);
        } else {
            grades = gradeRepository.findByStudent(student);
        }

        // Group grades by subject and calculate averages
        Map<Subject, List<Grades>> gradesBySubject = grades.stream()
                .collect(Collectors.groupingBy(Grades::getSubject));

        StudentGradesResponse response = new StudentGradesResponse();
        response.setStudentId(studentId);
        response.setStudentName(student.getFirstName() + " " + student.getLastName());
        response.setSemesterId(semesterId);

        // Calculate subject averages and overall GPA
        double totalWeightedSum = 0;
        double totalCredits = 0;

        for (Map.Entry<Subject, List<Grades>> entry : gradesBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Grades> subjectGrades = entry.getValue();

            double subjectAverage = calculateWeightedAverage(subjectGrades);
            totalWeightedSum += subjectAverage * subject.getCredits().doubleValue();
            totalCredits += subject.getCredits().doubleValue();

            // Add to response (implement SubjectGradeResponse DTO)
        }

        double gpa = totalCredits > 0 ? totalWeightedSum / totalCredits : 0;
        response.setGpa(gpa);

        return response;
    }

    @PreAuthorize("hasRole('TEACHER')")
    public List<GradeResponse> getTeacherGrades() {
        Users currentUser = authService.getCurrentUser();
        List<Grades> grades = gradeRepository.findGradesEnteredByTeacher(currentUser.getId());

        return grades.stream()
                .map(this::convertToGradeResponse)
                .collect(Collectors.toList());
    }

    private double calculateWeightedAverage(List<Grades> grades) {
        if (grades.isEmpty()) return 0;

        double totalWeightedSum = 0;
        double totalCoefficients = 0;

        for (Grades grade : grades) {
            totalWeightedSum += grade.getValue() * grade.getCoefficient();
            totalCoefficients += grade.getCoefficient();
        }

        return totalCoefficients > 0 ? totalWeightedSum / totalCoefficients : 0;
    }

    private GradeResponse convertToGradeResponse(Grades grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setStudentId(grade.getStudent().getId());
        response.setStudentName(grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName());
        response.setSubjectId(grade.getSubject().getId());
        response.setSubjectName(grade.getSubject().getName());
        response.setSemesterId(grade.getSemesters().getId());
        response.setSemesterName(grade.getSemesters().getName());
        response.setValue(grade.getValue());
        response.setCoefficient(grade.getCoefficient());
        response.setGradeType(grade.getGradeType());
        response.setComments(grade.getComments());
        response.setEnteredBy(grade.getEnteredBy().getFirstName() + " " + grade.getEnteredBy().getLastName());
        response.setCreatedDate(grade.getCreatedDate());
        return response;
    }
}
