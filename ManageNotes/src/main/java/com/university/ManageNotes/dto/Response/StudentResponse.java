package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.TeachingLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private TeachingLevel studentLevel;
    private StudentCycle cycle;
    private String matricule;
    private String speciality;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private List<GradeResponse> grades;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Boolean isActive;
    private Long semesterId;

    private List<StudentRequest> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
