package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    boolean existsByName(String name);
    
    Optional<Semester> findByActiveTrue();
}
