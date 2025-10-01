package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String name);
    
    boolean existsByDepartmentNameAndDepartmentIdNot(String name, Long departmentId);
}
