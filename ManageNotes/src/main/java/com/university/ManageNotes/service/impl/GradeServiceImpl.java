package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Response.*;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.GradeService;
import com.university.ManageNotes.service.RevendicationPeriodService;
import com.university.ManageNotes.util.GradeCalculator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final SemesterRepository semesterRepository;
    private final ExamRepository examRepository;
    private final RevendicationPeriodService revendicationPeriodService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GradeRequest createGrade(GradeRequest request) {
        // Map DTO to Entity
        Grades grade = modelMapper.map(request, Grades.class);
        
        // Get current teacher
        Teacher currentTeacher = getCurrentTeacher();
        
        // Get entities
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));
            
        Subject subject = subjectRepository.findById(request.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", request.getSubjectId()));
            
        Semester semester = semesterRepository.findById(request.getSemesterId())
            .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", request.getSemesterId()));
            
        Exam exam = examRepository.findById(request.getExamId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", request.getExamId()));
        
        // Validate teacher can enter grade for this subject and level
        if (!canTeacherEnterGrade(currentTeacher, subject, student)) {
            throw new APIException("You are not authorized to enter grades for this subject and level");
        }
        
        // Check if semester is active
        if (!semester.getActive()) {
            throw new APIException("Cannot enter grades for inactive semester");
        }
        
        // Validate period is open
        if (!revendicationPeriodService.isPeriodOpen(semester.getSemesterId(), exam)) {
            throw new APIException("Grade entry period is closed for " + exam.getAssessmentType());
        }
        
        // Check for duplicate grade entry
        if (gradeRepository.existsByStudentAndSubjectAndExamAndSemester(student, subject, exam, semester)) {
            throw new APIException("Grade already exists for this student, subject, and exam");
        }
        
        // Set relationships
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setExaminer(currentTeacher);
        grade.setSemester(semester);
        grade.setExam(exam);
        
        // Calculate total score from CC and SN
        double totalScore = GradeCalculator.calculateSubjectTotal(grade.getCcScore(), grade.getSnScore());
        grade.setTotalScore(totalScore);
        
        // Calculate and set derived values
        calculateGradeMetrics(grade);
        
        // Save and map back to DTO
        Grades savedGrade = gradeRepository.save(grade);
        return modelMapper.map(savedGrade, GradeRequest.class);
    }

    @Override
    @Transactional
    public GradeRequest updateGrade(Long gradeId, GradeRequest updateRequest) {
        // Get existing grade
        Grades existingGrade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", gradeId));
            
        // Validate teacher can update this grade
        Teacher currentTeacher = getCurrentTeacher();
        if (!existingGrade.getExaminer().getId().equals(currentTeacher.getId())) {
            throw new APIException("You can only update grades you entered");
        }
        
        // Check if revendication period is open for updates
        if (!revendicationPeriodService.isPeriodOpen(
                existingGrade.getSemester().getSemesterId(), 
                existingGrade.getExam())) {
            throw new APIException("Grade update period is closed");
        }
        
        // Update fields
        if (updateRequest.getCcScore() != null) {
            existingGrade.setCcScore(updateRequest.getCcScore());
        }
        if (updateRequest.getSnScore() != null) {
            existingGrade.setSnScore(updateRequest.getSnScore());
        }
        if (updateRequest.getComments() != null) {
            existingGrade.setComments(updateRequest.getComments());
        }
        
        // Recalculate total score
        double totalScore = GradeCalculator.calculateSubjectTotal(
            existingGrade.getCcScore(), existingGrade.getSnScore());
        existingGrade.setTotalScore(totalScore);
        
        // Recalculate metrics
        calculateGradeMetrics(existingGrade);
        
        // Save and map back to DTO
        Grades updatedGrade = gradeRepository.save(existingGrade);
        return modelMapper.map(updatedGrade, GradeRequest.class);
    }

    @Override
    @Transactional
    public MessageResponse deleteGrade(Long gradeId) {
        Grades grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", gradeId));
            
        // Validate teacher can delete this grade
        Teacher currentTeacher = getCurrentTeacher();
        if (!grade.getExaminer().getId().equals(currentTeacher.getId())) {
            throw new APIException("You can only delete grades you entered");
        }
        
        gradeRepository.delete(grade);
        return new MessageResponse("Grade deleted successfully");
    }

    @Override
    public StudentResponse getStudentGrades(Long studentId, Long semesterId) {
        // Get student
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        // Get semester (active if not specified)
        Semester semester = semesterId != null ? 
            semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", semesterId)) :
            getActiveSemester();
            
        // Get grades for student in semester for their level
        List<Grades> grades = gradeRepository.findByStudentAndSemester(student, semester);
        
        // Filter grades for student's level subjects only
        grades = grades.stream()
            .filter(grade -> grade.getSubject().getSubjectsLevel().stream()
                .anyMatch(level -> level.getStudentLevel().equals(student.getStudentLevel().getStudentLevel())))
            .toList();
        
        // Map to response DTOs
        List<GradeResponse> gradeResponses = grades.stream()
            .map(grade -> modelMapper.map(grade, GradeResponse.class))
            .collect(Collectors.toList());
            
        // Build student response with grades
        StudentResponse response = modelMapper.map(student, StudentResponse.class);
        response.setGrades(gradeResponses);
        
        return response;
    }

    @Override
    public List<GradeResponse> getTeacherGrades() {
        Teacher currentTeacher = getCurrentTeacher();
        
        List<Grades> grades = gradeRepository.findByExaminer(currentTeacher);
        
        return grades.stream()
            .map(grade -> modelMapper.map(grade, GradeResponse.class))
            .collect(Collectors.toList());
    }

    // Helper methods
    private Teacher getCurrentTeacher() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return teacherRepository.findById(userDetails.getId())
                .orElseThrow(() -> new APIException("Teacher not found"));
        }
        throw new APIException("No authenticated teacher");
    }
    
    private Semester getActiveSemester() {
        return semesterRepository.findByActiveTrue()
            .orElseThrow(() -> new APIException("No active semester found"));
    }
    
    private boolean canTeacherEnterGrade(Teacher teacher, Subject subject, Student student) {
        // Check if teacher teaches this subject at student's level
        return subject.getTeacher() != null &&
               subject.getTeacher().getId().equals(teacher.getId()) &&
               subject.getSubjectsLevel().stream()
                   .anyMatch(level -> level.getStudentLevel()
                           .equals(student.getStudentLevel().getStudentLevel()));
    }
    
    private void calculateGradeMetrics(Grades grade) {
        // Calculate GPA based on total score
        double gpa = GradeCalculator.calculateGPA(grade.getTotalScore());
        grade.setGpa(gpa);
        
        // Check if passed
        boolean passed = GradeCalculator.hasPassed(grade.getTotalScore());
        grade.setHasPassed(passed);
    }
}