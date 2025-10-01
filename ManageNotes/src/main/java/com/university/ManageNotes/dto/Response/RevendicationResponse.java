package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevendicationResponse {
    private Long revendicationId;
    private StudentResponse student;
    private GradeResponse grade;
    private SemesterResponse semester;
    private Double requestedScore;
    private String description;
    private String teacherComment;
    private RequestStatus status;
    private Instant createdDate;
    private Instant lastModifiedDate;

    private List<RevendicationRequest> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;


}
