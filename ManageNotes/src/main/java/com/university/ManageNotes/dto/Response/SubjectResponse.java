package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.StudentCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private Long subjectId;
    private String subjectCode;
    private BigDecimal credits;
    private String description;
    private TeacherResponse teacher;
    private List<TeachingLevel> subjectsLevel;
    private StudentCycle Studentcycle;
    private SemesterResponse semester;
    private DepartmentResponse department;

    private List<SubjectRequest> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
