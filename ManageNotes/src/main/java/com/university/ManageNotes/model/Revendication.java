package com.university.ManageNotes.model;

import com.university.ManageNotes.model.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade_claims")
public class Revendication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revendicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Exam period;

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grades grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "requested_score", nullable = false)
    @Min(value = 0, message = "Requested score must be greater than or equal to 0")
    @NotBlank(message = "Requested score is required")
    private Double requestedScore;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description is required")
    private String description;

    @Column(name = "teacher_comment", columnDefinition = "TEXT")
    @NotBlank(message = "Teacher comment is required")
    private String teacherComment = "Pending review";

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @CreatedDate
    @Column(name ="creation_date",nullable = false,updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}
