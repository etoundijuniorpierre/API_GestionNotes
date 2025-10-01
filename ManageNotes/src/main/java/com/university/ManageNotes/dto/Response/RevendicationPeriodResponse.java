package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevendicationPeriodResponse {
    private Long revendicationPeriodId;
    private ExamResponse exam;
    private SemesterResponse semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isActive;
    private Instant createdDate;
    private Instant lastModifiedDate;

    // Pagination support
    private List<RevendicationPeriodResponse> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}