package com.university.ManageNotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Subject extends AbstractEntity{

     @Column(name = "name")
     private String name;

     @Column(name = "code")
     private String code;

     @Column(name = "credits")
     private BigDecimal credits;

     @Column(name = "coefficient")
     private BigDecimal coefficient;

     @Column(name = "idTeacher")
     private Long idTeacher;

     @OneToMany(mappedBy = "subject")
     private List<Grades> grades;

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getCode() {
          return code;
     }

     public void setCode(String code) {
          this.code = code;
     }

     public BigDecimal getCredits() {
          return credits;
     }

     public void setCredits(BigDecimal credits) {
          this.credits = credits;
     }

     public BigDecimal getCoefficient() {
          return coefficient;
     }

     public void setCoefficient(BigDecimal coefficient) {
          this.coefficient = coefficient;
     }

     public Long getIdTeacher() {
          return idTeacher;
     }

     public void setIdTeacher(Long idTeacher) {
          this.idTeacher = idTeacher;
     }

     public List<Grades> getGrades() {
          return grades;
     }

     public void setGrades(List<Grades> grades) {
          this.grades = grades;
     }
}
