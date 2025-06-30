package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Semesters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semesters, Long> {

    List<Semesters> findByActiveTrue();

    Optional<Semesters> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT s FROM Semesters s WHERE s.startDate <= :date AND s.endDate >= :date")
    Optional<Semesters> findCurrentSemester(LocalDate date);

    @Query("SELECT s FROM Semesters s ORDER BY s.startDate DESC")
    List<Semesters> findAllOrderByStartDateDesc();

    @Query("SELECT s FROM Semesters s WHERE s.active = true ORDER BY s.startDate DESC")
    List<Semesters> findActiveSemestersOrderByStartDate();
}
