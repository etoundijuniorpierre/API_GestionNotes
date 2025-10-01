package com.university.ManageNotes.model;

import com.university.ManageNotes.model.enums.AssessmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exam_periods")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examPeriodId;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    @Column(length = 20)
    private AssessmentType assessmentType;
}
