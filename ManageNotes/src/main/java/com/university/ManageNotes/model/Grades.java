package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grades extends AbstractEntity{

 @Column(name = "value")
 private Double value;

 @Column(name = "coefficient")
 private Double coefficient;

 @Column(name = "Comments")
 private String comments;

 @ManyToOne
 @JoinColumn(name = "idStudents")
 private Students student;

 @ManyToOne
 @JoinColumn(name = "idSubject")
 private Subject subject;

 @ManyToOne
 @JoinColumn(name = "idUsers")
 private Users enteredBy;

 @ManyToOne
 @JoinColumn(name = "idSemester")
 private   Semesters semesters;

 @Enumerated(EnumType.STRING)
 @Column(name = "gradeType")
 private GradeType gradeType;
}
