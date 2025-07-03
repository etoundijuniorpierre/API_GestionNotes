package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Grades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grades, Long> {

    List<Grades> findByStudentId(Long studentId);

    List<Grades> findBySubjectId(Long subjectId);

    List<Grades> findBySemesterId(Long semesterId);

    List<Grades> findByEnteredById(Long teacherId);

    List<Grades> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

    List<Grades> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
