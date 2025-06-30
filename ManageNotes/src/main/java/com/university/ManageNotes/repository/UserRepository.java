package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Users> findByRole(Role role);

    List<Users> findByActiveTrue();

    @Query("SELECT u FROM Users u WHERE u.role = :role AND u.active = true")
    List<Users> findActiveUsersByRole(@Param("role") Role role);

    @Query("SELECT u FROM Users u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<Users> findByNameContaining(@Param("name") String name);
}
