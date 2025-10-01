package com.university.ManageNotes.model;

import com.university.ManageNotes.model.enums.AssessmentType;
import com.university.ManageNotes.util.GradeCalculator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "grades")
public class Grades{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    private Double ccScore;
    
    private Double snScore;
    
    // Calculated field - total score on 100
    private Double totalScore;

    private String comments;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher examiner;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @OneToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @OneToMany(mappedBy = "grade", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    orphanRemoval = true)
    private List<Revendication> revendication = new ArrayList<>();

    @CreatedDate
    @Column(name ="creation_date",nullable = false,updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    private Boolean hasPassed;

    private Double gpa;

    public Grades(Double ccScore, Double snScore, Student student, Subject subject, String comments, Teacher examiner, Semester semester, Exam exam) {
        this.ccScore = ccScore;
        this.snScore = snScore;
        this.student = student;
        this.subject = subject;
        this.comments = comments;
        this.examiner = examiner;
        this.semester = semester;
        this.exam = exam;
        this.totalScore = GradeCalculator.calculateSubjectTotal(ccScore, snScore);
    }
}
