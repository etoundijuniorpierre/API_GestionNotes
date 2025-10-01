package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByAppRole(AppRole appRole);
}
