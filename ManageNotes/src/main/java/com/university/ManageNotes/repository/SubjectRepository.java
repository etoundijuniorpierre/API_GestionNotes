package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.TeachingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findSubjectByTeacher_Email(String teacherEmail);

    Optional<Subject> findBySubjectCode(String code);

    boolean existsByTeacherIdAndSubjectsLevel(Long teacherId, List<TeachingLevel> level);
}
