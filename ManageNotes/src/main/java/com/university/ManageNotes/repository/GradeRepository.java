package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.GradeType;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Semesters;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grades, Long> {

    List<Grades> findByStudent(Students student);

    List<Grades> findBySubject(Subject subject);

    List<Grades> findBySemesters(Semesters semester);

    List<Grades> findByStudentAndSubject(Students student, Subject subject);

    List<Grades> findByStudentAndSemesters(Students student, Semesters semester);

    List<Grades> findByStudentAndSubjectAndSemesters(Students student, Subject subject, Semesters semester);

    List<Grades> findByGradeType(GradeType gradeType);

    @Query("SELECT g FROM Grades g WHERE g.student.id = :studentId AND g.semesters.id = :semesterId")
    List<Grades> findByStudentIdAndSemesterId(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);

    @Query("SELECT AVG(g.value) FROM Grades g WHERE g.student = :student AND g.subject = :subject AND g.semesters = :semester")
    Double calculateAverageGrade(@Param("student") Students student, @Param("subject") Subject subject, @Param("semester") Semesters semester);

    @Query("SELECT g FROM Grades g WHERE g.enteredBy.id = :teacherId")
    List<Grades> findGradesEnteredByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT DISTINCT g.student FROM Grades g WHERE g.subject.id = :subjectId")
    List<Students> findStudentsBySubject(@Param("subjectId") Long subjectId);
}
