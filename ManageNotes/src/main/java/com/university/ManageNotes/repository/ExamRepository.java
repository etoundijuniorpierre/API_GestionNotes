package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Exam;
import com.university.ManageNotes.model.enums.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
}