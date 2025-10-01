package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterResponse {
    private Long semesterId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Instant createdDate;
    private Instant lastModifiedDate;
    
    // Using Response DTOs instead of entities and Set to avoid duplicates
    private Set<SubjectResponse> subjects;
    private Set<GradeResponse> grades;
    
    // Pagination support
    private List<SemesterResponse> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}