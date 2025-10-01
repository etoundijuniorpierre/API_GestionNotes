package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.RevendicationResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.RequestStatus;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.RevendicationPeriodService;
import com.university.ManageNotes.service.RevendicationService;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevendicationServiceImpl implements RevendicationService {
    
    private final RevendicationRepository revendicationRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GradeRepository gradeRepository;
    private final SemesterRepository semesterRepository;
    private final ExamRepository examRepository;
    private final RevendicationPeriodService revendicationPeriodService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RevendicationRequest createRevendication(RevendicationRequest request) {
        // Map DTO to Entity
        Revendication revendication = modelMapper.map(request, Revendication.class);
        
        // Get current student
        Student currentStudent = getCurrentStudent();
        
        // Get entities
        Grades grade = gradeRepository.findById(request.getGrade().getGradeId())
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", request.getGrade().getGradeId()));
            
        Exam exam = examRepository.findById(request.getPeriod().getExamPeriodId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", request.getPeriod().getExamPeriodId()));
        
        // Validate student owns this grade
        if (!grade.getStudent().getId().equals(currentStudent.getId())) {
            throw new APIException("You can only create revendications for your own grades");
        }
        
        // Get active semester
        Semester activeSemester = getActiveSemester();
        
        // Check if revendication period is open
        if (!revendicationPeriodService.isPeriodOpen(activeSemester.getSemesterId(), exam)) {
            throw new APIException("Revendication period is closed for " + exam.getAssessmentType());
        }
        
        // Check for duplicate revendication
        if (revendicationRepository.existsByStudentAndGradeAndStatus(currentStudent, grade, RequestStatus.PENDING)) {
            throw new APIException("You already have a pending revendication for this grade");
        }
        
        // Set relationships
        revendication.setStudent(currentStudent);
        revendication.setGrade(grade);
        revendication.setPeriod(exam);
        revendication.setSemester(activeSemester);
        revendication.setStatus(RequestStatus.PENDING);
        revendication.setTeacherComment("Pending review");
        
        // Save and map back to DTO
        Revendication savedRevendication = revendicationRepository.save(revendication);
        return modelMapper.map(savedRevendication, RevendicationRequest.class);
    }

    @Override
    public RevendicationResponse getRevendicationForTeacher(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // Get current teacher
        Teacher currentTeacher = getCurrentTeacher();
        
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase(AppConstant.SORT_DIR) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        
        // Get pending revendications for teacher's subjects
        Page<Revendication> revendicationPage = revendicationRepository
            .findByGrade_Subject_TeacherAndStatusOrderByCreatedDateDesc(currentTeacher, RequestStatus.PENDING, pageable);

        List<Revendication> revendications = revendicationPage.getContent();
        if (revendications.isEmpty()) {
            throw new APIException("No pending revendications found");
        }

        // Map to request DTOs for content
        List<RevendicationRequest> revendicationRequests = revendications.stream()
                .map(rev -> modelMapper.map(rev, RevendicationRequest.class))
                .collect(Collectors.toList());

        // Build paginated response
        RevendicationResponse response = new RevendicationResponse();
        response.setContent(revendicationRequests);
        response.setPageNumber(revendicationPage.getNumber());
        response.setPageSize(revendicationPage.getSize());
        response.setTotalElements(revendicationPage.getTotalElements());
        response.setTotalPages(revendicationPage.getTotalPages());
        response.setLastPage(revendicationPage.isLast());

        return response;
    }

    @Override
    @Transactional
    public MessageResponse approveRevendication(Long revendicationId, String teacherComment) {
        // Get revendication entity
        Revendication revendication = revendicationRepository.findById(revendicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Revendication", "id", revendicationId));
        
        // Validate teacher can approve this revendication
        Teacher currentTeacher = getCurrentTeacher();
        if (!revendication.getGrade().getSubject().getTeacher().getId().equals(currentTeacher.getId())) {
            throw new APIException("You can only approve revendications for your subjects");
        }
        
        // Validate revendication is pending
        if (!revendication.getStatus().equals(RequestStatus.PENDING)) {
            throw new APIException("Only pending revendications can be approved");
        }
        
        // Update revendication status
        revendication.setStatus(RequestStatus.APPROVED);
        revendication.setTeacherComment(teacherComment != null ? teacherComment : "Approved");
        
        // Update the associated grade with requested score
        Grades grade = revendication.getGrade();
        grade.setCcScore(revendication.getGrade().getCcScore());
        grade.setSnScore(revendication.getGrade().getSnScore());
        grade.setGpa(revendication.getGrade().getGpa());
        grade.setHasPassed(revendication.getGrade().getHasPassed());
        grade.setComments(revendication.getGrade().getComments());
        grade.setTotalScore(revendication.getRequestedScore());
        
        // Recalculate grade metrics
        calculateGradeMetrics(grade);
        
        // Save entities
        gradeRepository.save(grade);
        revendicationRepository.save(revendication);
        
        return new MessageResponse("Revendication approved successfully");
    }

    @Override
    @Transactional
    public MessageResponse rejectRevendication(Long revendicationId, String reason) {
        // Get revendication entity
        Revendication revendication = revendicationRepository.findById(revendicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Revendication", "id", revendicationId));
        
        // Validate teacher can reject this revendication
        Teacher currentTeacher = getCurrentTeacher();
        if (!revendication.getGrade().getSubject().getTeacher().getId().equals(currentTeacher.getId())) {
            throw new APIException("You can only reject revendications for your subjects");
        }
        
        // Validate revendication is pending
        if (!revendication.getStatus().equals(RequestStatus.PENDING)) {
            throw new APIException("Only pending revendications can be rejected");
        }
        
        // Update revendication status
        revendication.setStatus(RequestStatus.REJECTED);
        revendication.setTeacherComment(reason != null ? reason : "Rejected");
        
        // Save and map back to DTO
        revendicationRepository.save(revendication);
        
        return new MessageResponse("Revendication rejected successfully");
    }

    @Override
    public List<RevendicationResponse> getStudentRevendications(Long studentId) {
        // Get student
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        // Validate student can view their own revendications
        Student currentStudent = getCurrentStudent();
        if (!student.getId().equals(currentStudent.getId())) {
            throw new APIException("You can only view your own revendications");
        }
        
        // Get student's revendications
        List<Revendication> revendications = revendicationRepository.findByStudentOrderByCreatedDateDesc(student);
        
        // Map to response DTOs
        return revendications.stream()
            .map(rev -> modelMapper.map(rev, RevendicationResponse.class))
            .collect(Collectors.toList());
    }

    // Helper methods
    private Student getCurrentStudent() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return studentRepository.findById(userDetails.getId())
                .orElseThrow(() -> new APIException("Student not found"));
        }
        throw new APIException("No authenticated student");
    }
    
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
    
    private void calculateGradeMetrics(Grades grade) {
        // Calculate GPA based on total score
        double gpa = calculateGPA(grade.getTotalScore());
        grade.setGpa(gpa);
        
        // Check if passed
        boolean passed = grade.getTotalScore() >= 50.0;
        grade.setHasPassed(passed);
    }
    
    private double calculateGPA(double scoreOn100) {
        if (scoreOn100 < 35) return 0.0;
        if (scoreOn100 < 40) return 1.0;
        if (scoreOn100 < 45) return 1.3;
        if (scoreOn100 < 50) return 1.7;
        if (scoreOn100 < 55) return 2.0;
        if (scoreOn100 < 60) return 2.3;
        if (scoreOn100 < 65) return 2.7;
        if (scoreOn100 < 70) return 3.0;
        if (scoreOn100 < 75) return 3.3;
        if (scoreOn100 < 80) return 3.7;
        return 4.0;
    }
}