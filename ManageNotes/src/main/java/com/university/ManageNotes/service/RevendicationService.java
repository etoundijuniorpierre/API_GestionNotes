package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.dto.Response.RevendicationResponse;

import java.util.List;

public interface RevendicationService {
    
    RevendicationRequest createRevendication(RevendicationRequest req);
    
    RevendicationResponse getRevendicationForTeacher(Integer pageNumber,
                                                     Integer pageSize,
                                                     String sortBy, String sortOrder);

    MessageResponse approveRevendication(Long revendicationId, String teacherComment);

    MessageResponse rejectRevendication(Long revendicationId, String reason);

    List<RevendicationResponse> getStudentRevendications(Long studentId);
}
