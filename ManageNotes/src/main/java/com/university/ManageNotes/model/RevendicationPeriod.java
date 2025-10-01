package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revendication_period")
public class RevendicationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revendicationPeriodId;

    @OneToOne
    @JoinColumn(name = "exam_period_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @NotBlank(message = "The start date of the revendication period is required")
    private LocalDate startDate;

    @NotBlank(message = "The start date of the revendication period is required")
    private LocalDate endDate;

    private String color;

    private Boolean isActive = false;

    @CreatedDate
    @Column(name ="creation_date",nullable = false,updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

}
