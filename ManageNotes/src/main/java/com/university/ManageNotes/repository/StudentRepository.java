package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {

    Optional<Students> findByStudentNumber(String studentNumber);

    Optional<Students> findByEmail(String email);

    boolean existsByStudentNumber(String studentNumber);

    boolean existsByEmail(String email);

    List<Students> findByLevel(String level);

    @Query("SELECT s FROM Students s WHERE s.firstName LIKE %:name% OR s.lastName LIKE %:name%")
    List<Students> findByNameContaining(@Param("name") String name);

    @Query("SELECT s FROM Students s ORDER BY s.firstName, s.lastName")
    List<Students> findAllOrderByName();
}
