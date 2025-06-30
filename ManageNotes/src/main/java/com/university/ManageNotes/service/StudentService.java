package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Students createStudent(Students student) {
        // Generate student number if not provided
        if (student.getStudentNumber() == null || student.getStudentNumber().isEmpty()) {
            student.setStudentNumber(generateStudentNumber());
        }

        // Validate unique constraints
        if (studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new RuntimeException("Student number already exists");
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return studentRepository.save(student);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Students updateStudent(Long studentId, Students studentDetails) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update fields
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setLevel(studentDetails.getLevel());

        // Check email uniqueness if changed
        if (!student.getEmail().equals(studentDetails.getEmail())) {
            if (studentRepository.existsByEmail(studentDetails.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            student.setEmail(studentDetails.getEmail());
        }

        return studentRepository.save(student);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(Long studentId) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
    }

    public Optional<Students> getStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public Optional<Students> getStudentByNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    public Optional<Students> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public List<Students> getAllStudents() {
        return studentRepository.findAllOrderByName();
    }

    public List<Students> getStudentsByLevel(String level) {
        return studentRepository.findByLevel(level);
    }

    public List<Students> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    private String generateStudentNumber() {
        // Generate a unique student number
        // You can implement your own logic here
        long count = studentRepository.count();
        return String.format("STU%06d", count + 1);
    }
}
