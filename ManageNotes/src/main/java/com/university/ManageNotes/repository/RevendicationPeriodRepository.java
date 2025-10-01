package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Exam;
import com.university.ManageNotes.model.RevendicationPeriod;
import com.university.ManageNotes.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RevendicationPeriodRepository extends JpaRepository<RevendicationPeriod, Long> {
    
    boolean existsByExamAndSemester(Exam exam, Semester semester);
    
    List<RevendicationPeriod> findBySemesterAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Semester semester, LocalDate startDate, LocalDate endDate);
    
    boolean existsByExamAndSemesterAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Exam exam, Semester semester, LocalDate startDate, LocalDate endDate);
}
