package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Response.TranscriptResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.StudentLevel;
import com.university.ManageNotes.model.enums.TranscriptStatus;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import com.university.ManageNotes.service.TranscriptService;
import com.university.ManageNotes.util.GradeCalculator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranscriptServiceImpl implements TranscriptService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public TranscriptResponse getTranscriptStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        return generateTranscript(student);
    }

    @Override
    public TranscriptResponse getStudentTranscript(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Student student = studentRepository.findById(userDetails.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", userDetails.getId()));
        
        return generateTranscript(student);
    }

    @Override
    public List<TranscriptResponse> getTranscriptByTeachingLevel(TeachingLevel level) {
        List<Student> students = studentRepository.findAll().stream()
            .filter(s -> s.getStudentLevel().equals(level))
            .collect(Collectors.toList());
        
        return students.stream()
            .map(this::generateTranscript)
            .collect(Collectors.toList());
    }

    private TranscriptResponse generateTranscript(Student student) {
        // Get all grades for the student
        List<Grades> allGrades = student.getGrades();
        
        if (allGrades.isEmpty()) {
            throw new APIException("No grades found for student");
        }
        
        // Group grades by semester
        Map<Long, List<Grades>> gradesBySemester = allGrades.stream()
            .collect(Collectors.groupingBy(grade -> grade.getSemester().getSemesterId()));
        
        // Calculate transcript data
        TranscriptResponse response = new TranscriptResponse();
        response.setStudentFirstName(student.getFirstName());
        response.setStudentLastName(student.getLastName());
        response.setStudentMatricule(student.getMatricule());
        response.setStudentLevel(student.getStudentLevel());
        response.setStudentCycle(student.getCycle());
        response.setStudentGrades(allGrades.stream()
            .map(grade -> modelMapper.map(grade, com.university.ManageNotes.dto.Response.GradeResponse.class))
            .collect(Collectors.toList()));
        
        // Calculate academic metrics
        calculateAcademicMetrics(response, gradesBySemester);
        
        // Determine promotion status
        determinePromotionStatus(response, student);
        
        return response;
    }

    private void calculateAcademicMetrics(TranscriptResponse response, Map<Long, List<Grades>> gradesBySemester) {
        double totalWeightedGPA = 0.0;
        double totalWeightedScore = 0.0;
        int totalCredits = 0;
        int creditsEarned = 0;
        
        double semester1Average = 0.0;
        double semester2Average = 0.0;
        int semester1Credits = 0;
        int semester2Credits = 0;
        
        boolean isFirstSemester = true;
        
        for (Map.Entry<Long, List<Grades>> entry : gradesBySemester.entrySet()) {
            List<Grades> semesterGrades = entry.getValue();
            
            double semesterWeightedGPA = 0.0;
            double semesterWeightedScore = 0.0;
            int semesterCredits = 0;
            
            for (Grades grade : semesterGrades) {
                int subjectCredits = grade.getSubject().getCredits().intValue();
                double totalScore = grade.getTotalScore();
                double scoreOn20 = GradeCalculator.convertTo20Scale(totalScore);
                double gpa = GradeCalculator.calculateGPA(totalScore);
                
                // Calculate weighted values
                semesterWeightedGPA += GradeCalculator.calculateWeightedGPA(gpa, subjectCredits);
                semesterWeightedScore += GradeCalculator.calculateWeightedScore(scoreOn20, subjectCredits);
                semesterCredits += subjectCredits;
                
                // Check if student passed this subject
                if (GradeCalculator.hasPassed(totalScore)) {
                    creditsEarned += subjectCredits;
                }
            }
            
            // Calculate semester averages
            double semesterAverage = semesterCredits > 0 ? semesterWeightedScore / semesterCredits : 0.0;
            
            if (isFirstSemester) {
                semester1Average = semesterAverage;
                semester1Credits = semesterCredits;
                isFirstSemester = false;
            } else {
                semester2Average = semesterAverage;
                semester2Credits = semesterCredits;
            }
            
            totalWeightedGPA += semesterWeightedGPA;
            totalWeightedScore += semesterWeightedScore;
            totalCredits += semesterCredits;
        }
        
        // Calculate final averages
        double finalGPA = totalCredits > 0 ? totalWeightedGPA / totalCredits : 0.0;
        double finalAverageOn20 = totalCredits > 0 ? totalWeightedScore / totalCredits : 0.0;
        
        // Set response values
        response.setAnnualAverage(finalAverageOn20);
        response.setCreditsEarned(creditsEarned);
        response.setTotalCreditsRequired(totalCredits);
        response.setSemester1Average(semester1Average);
        response.setSemester2Average(semester2Average);
        response.setSemester1Credits(semester1Credits);
        response.setSemester2Credits(semester2Credits);
    }

    private void determinePromotionStatus(TranscriptResponse response, Student student) {
        double finalGPA = calculateFinalGPA(response);
        int creditsEarned = response.getCreditsEarned();
        int totalCredits = response.getTotalCreditsRequired();
        
        // Check GPA requirement (>= 2.0)
        boolean gpaRequirementMet = finalGPA >= 2.0;
        
        // Check credit requirements based on level
        boolean creditRequirementMet;
        StudentLevel levelEnum = student.getStudentLevel().getStudentLevel();
        String levelName = levelEnum.name();
        
        if ("LEVEL1".equals(levelName)) {
            // Level 1: 90% of credits required
            creditRequirementMet = creditsEarned >= (totalCredits * 0.9);
        } else {
            // Level 2+: 100% of credits required
            creditRequirementMet = creditsEarned >= totalCredits;
        }
        
        // Determine final status
        if (gpaRequirementMet && creditRequirementMet) {
            response.setStatus(TranscriptStatus.PASSED);
        } else {
            response.setStatus(TranscriptStatus.FAILED);
        }
    }

    private double calculateFinalGPA(TranscriptResponse response) {
        // Calculate GPA from the annual average
        double averageOn100 = response.getAnnualAverage() * 5.0; // Convert from 20 to 100 scale
        return GradeCalculator.calculateGPA(averageOn100);
    }
}