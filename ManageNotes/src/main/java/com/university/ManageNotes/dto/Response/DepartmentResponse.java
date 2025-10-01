package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class DepartmentResponse {
    // Individual department fields for single operations
    private Long departmentId;
    private String departmentName;
    private Set<SubjectResponse> departmentSubjects;
    private Instant createdDate;
    private Instant lastModifiedDate;
    
    // List for paginated operations
    private List<DepartmentRequest> content;
    private Set<SubjectResponse> subjects; // All subjects from all departments
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
