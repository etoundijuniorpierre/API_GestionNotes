package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
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
 private Semesters semester;

 @Enumerated(EnumType.STRING)
 @Column(name = "gradeType")
 private GradeType type;

 // Backward compatibility methods
 public GradeType getGradeType() {
  return this.type;
 }

 public void setGradeType(GradeType gradeType) {
  this.type = gradeType;
 }

 public Semesters getSemesters() {
  return this.semester;
 }

 public void setSemesters(Semesters semesters) {
  this.semester = semesters;
 }


 public Students getStudent() {
  return student;
 }

 public Subject getSubject() {
  return subject;
 }

 public Double getValue() {
  return value;
 }

 public Double getCoefficient() {
  return coefficient;
 }

 public String getComments() {
  return comments;
 }

 public Users getEnteredBy() {
  return enteredBy;
 }

 public GradeType getType() {
  return type;
 }
}
