package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Exam;
import com.university.ManageNotes.model.RevendicationPeriod;
import com.university.ManageNotes.model.Semester;
import com.university.ManageNotes.model.enums.AssessmentType;
import com.university.ManageNotes.repository.ExamRepository;
import com.university.ManageNotes.repository.RevendicationPeriodRepository;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.service.RevendicationPeriodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevendicationPeriodServiceImpl implements RevendicationPeriodService {
    
    private final RevendicationPeriodRepository revendicationPeriodRepository;
    private final SemesterRepository semesterRepository;
    private final ExamRepository examRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RevendicationPeriodRequest createPeriod(RevendicationPeriodRequest request) {
        // Map DTO to Entity
        RevendicationPeriod period = modelMapper.map(request, RevendicationPeriod.class);
        
        // Get active semester
        Semester activeSemester = getActiveSemester();
        
        // Get exam
        Exam exam = examRepository.findById(request.getExamId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", request.getExamId()));
        
        // Check for duplicate period for same exam and semester
        if (revendicationPeriodRepository.existsByExamAndSemester(exam, activeSemester)) {
            throw new APIException("Revendication period already exists for this exam in active semester");
        }
        
        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new APIException("Start date cannot be after end date");
        }
        
        // Set relationships and properties
        period.setExam(exam);
        period.setSemester(activeSemester);
        period.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        // Save entity and map to request DTO
        RevendicationPeriod savedPeriod = revendicationPeriodRepository.save(period);
        return modelMapper.map(savedPeriod, RevendicationPeriodRequest.class);
    }

    @Override
    public List<RevendicationPeriodResponse> getAllPeriod() {
        List<RevendicationPeriod> periods = revendicationPeriodRepository.findAll();
        
        return periods.stream()
            .map(period -> modelMapper.map(period, RevendicationPeriodResponse.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RevendicationPeriodRequest updatePeriod(Long id, RevendicationPeriodRequest request) {
        RevendicationPeriod existingPeriod = revendicationPeriodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("RevendicationPeriod", "id", id));
        
        // Update fields
        if (request.getStartDate() != null) {
            existingPeriod.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            existingPeriod.setEndDate(request.getEndDate());
        }
        if (request.getColor() != null) {
            existingPeriod.setColor(request.getColor());
        }
        if (request.getIsActive() != null) {
            existingPeriod.setIsActive(request.getIsActive());
        }
        
        // Update exam if provided
        if (request.getExamId() != null) {
            Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", request.getExamId()));
            
            // Check for duplicate if exam is being changed
            if (!existingPeriod.getExam().getExamPeriodId().equals(exam.getExamPeriodId()) &&
                revendicationPeriodRepository.existsByExamAndSemester(exam, existingPeriod.getSemester())) {
                throw new APIException("Revendication period already exists for this exam in the semester");
            }
            
            existingPeriod.setExam(exam);
        }
        
        // Validate dates
        if (existingPeriod.getStartDate().isAfter(existingPeriod.getEndDate())) {
            throw new APIException("Start date cannot be after end date");
        }
        
        // Save entity and map to request DTO
        RevendicationPeriod updatedPeriod = revendicationPeriodRepository.save(existingPeriod);
        return modelMapper.map(updatedPeriod, RevendicationPeriodRequest.class);
    }

    @Override
    @Transactional
    public void deletePeriod(Long id) {
        RevendicationPeriod period = revendicationPeriodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("RevendicationPeriod", "id", id));
        
        revendicationPeriodRepository.delete(period);
    }

    @Override
    public List<RevendicationPeriodResponse> getActivePeriods() {
        Semester activeSemester = getActiveSemester();
        LocalDate today = LocalDate.now();
        
        List<RevendicationPeriod> activePeriods = revendicationPeriodRepository
            .findBySemesterAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                activeSemester, today, today);
        
        return activePeriods.stream()
            .map(period -> modelMapper.map(period, RevendicationPeriodResponse.class))
            .collect(Collectors.toList());
    }

    @Override
    public Boolean isPeriodOpen(Long semesterId, Exam exam) {
        Semester semester = semesterRepository.findById(semesterId)
            .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", semesterId));
        
        LocalDate today = LocalDate.now();
        
        return revendicationPeriodRepository.existsByExamAndSemesterAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            exam, semester, today, today);
    }

    @Override
    public String getPeriodStatusMessage(Long semesterId, Exam exam) {
        if (isPeriodOpen(semesterId, exam)) {
            return "Revendication period is open for " + exam.getAssessmentType();
        } else {
            return "Revendication period is closed for " + exam.getAssessmentType();
        }
    }

    // Helper method
    private Semester getActiveSemester() {
        return semesterRepository.findByActiveTrue()
            .orElseThrow(() -> new APIException("No active semester found. Please activate a semester first."));
    }
}