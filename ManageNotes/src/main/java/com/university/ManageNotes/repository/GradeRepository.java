package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.TeachingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grades, Long> {

    List<Grades> findByStudentAndSemester(Student student, Semester semester);
    
    List<Grades> findByExaminer(Teacher teacher);

    boolean existsByStudentAndSubjectAndExamAndSemester(Student student, Subject subject, Exam exam, Semester semester);
}