package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.UserRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.service.StudentService;
import com.university.ManageNotes.service.SubjectService;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administration", description = "Administrative management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // User Management
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve all users in the system")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    @Operation(summary = "Create user", description = "Create a new user account")
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Email is already in use!", "ERROR", null));
            }

            Users user = new Users();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRole(userRequest.getRole());
            user.setActive(true);

            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User created successfully!", "SUCCESS",null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error creating user: " + e.getMessage(), "ERROR", null));
        }
    }

    @PutMapping("/users/{userId}")
    @Operation(summary = "Update user", description = "Update user information")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long userId,
                                                     @Valid @RequestBody UserRequest userRequest) {
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setRole(userRequest.getRole());
            user.setActive(userRequest.getActive());

            // Update email if changed
            if (!user.getEmail().equals(userRequest.getEmail())) {
                if (userRepository.existsByEmail(userRequest.getEmail())) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Error: Email is already in use!", "ERROR", null));
                }
                user.setEmail(userRequest.getEmail());
            }

            // Update password if provided
            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            }

            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User updated successfully!","SUCCESS",null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating user: " + e.getMessage(), "ERROR", null));
        }
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Delete user", description = "Delete a user account")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long userId) {
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userRepository.delete(user);
            return ResponseEntity.ok(new MessageResponse("deleting user succsefully","SUCCESS",null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error deleting user: " + e.getMessage() , "ERROR", null));
        }
    }

    // Student Management
    @GetMapping("/students")
    @Operation(summary = "Get all students", description = "Retrieve all students")
    public ResponseEntity<List<Students>> getAllStudents() {
        List<Students> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/students")
    @Operation(summary = "Create student", description = "Create a new student")
    public ResponseEntity<MessageResponse> createStudent(@Valid @RequestBody Students student) {
        try {
            studentService.createStudent(student);
            return ResponseEntity.ok(new MessageResponse("Student created successfully!", "SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error creating student: " + e.getMessage(), "ERROR", null));
        }
    }

    @PutMapping("/students/{studentId}")
    @Operation(summary = "Update student", description = "Update student information")
    public ResponseEntity<MessageResponse> updateStudent(@PathVariable Long studentId,
                                                         @Valid @RequestBody Students studentDetails) {
        try {
            studentService.updateStudent(studentId, studentDetails);
            return ResponseEntity.ok(new MessageResponse("Student updated successfully!", "SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating student: " + e.getMessage(), "ERRRR", null));
        }
    }

    @DeleteMapping("/students/{studentId}")
    @Operation(summary = "Delete student", description = "Delete a student")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable Long studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.ok(new MessageResponse("Student deleted successfully!", "SUCCESS", null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error deleting student: " + e.getMessage(), "ERROR", null));
        }
    }

    // Subject Management
    @GetMapping("/subjects")
    @Operation(summary = "Get all subjects", description = "Retrieve all subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/subjects")
    @Operation(summary = "Create subject", description = "Create a new subject")
    public ResponseEntity<MessageResponse> createSubject(@Valid @RequestBody Subject subject) {
        try {
            subjectService.createSubject(subject);
            return ResponseEntity.ok(new MessageResponse("Subject created successfully!", "SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error creating subject: " + e.getMessage(), "ERROR", null));
        }
    }

    @PutMapping("/subjects/{subjectId}")
    @Operation(summary = "Update subject", description = "Update subject information")
    public ResponseEntity<MessageResponse> updateSubject(@PathVariable Long subjectId,
                                                         @Valid @RequestBody Subject subjectDetails) {
        try {
            subjectService.updateSubject(subjectId, subjectDetails);
            return ResponseEntity.ok(new MessageResponse("Subject updated successfully!", "SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating subject: " + e.getMessage(), "ERROR", null));
        }
    }

    @DeleteMapping("/subjects/{subjectId}")
    @Operation(summary = "Delete subject", description = "Delete a subject")
    public ResponseEntity<MessageResponse> deleteSubject(@PathVariable Long subjectId) {
        try {
            subjectService.deleteSubject(subjectId);
            return ResponseEntity.ok(new MessageResponse("Subject deleted successfully!", "SUCCESS",null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error deleting subject: " + e.getMessage(), "ERROR", null));
        }
    }

    // Semester Management
    @GetMapping("/semesters")
    @Operation(summary = "Get all semesters", description = "Retrieve all semesters")
    public ResponseEntity<List<Semesters>> getAllSemesters() {
        List<Semesters> semesters = semesterRepository.findAllOrderByStartDateDesc();
        return ResponseEntity.ok(semesters);
    }

    @PostMapping("/semesters")
    @Operation(summary = "Create semester", description = "Create a new semester")
    public ResponseEntity<MessageResponse> createSemester(@Valid @RequestBody Semesters semester) {
        try {
            if (semesterRepository.existsByName(semester.getName())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Semester name already exists!", "ERROR", null));
            }
            semesterRepository.save(semester);
            return ResponseEntity.ok(new MessageResponse("Semester created successfully!", "SUCCESS", null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error creating semester: " + e.getMessage(), "ERROR", null));
        }
    }

    @PutMapping("/semesters/{semesterId}")
    @Operation(summary = "Update semester", description = "Update semester information")
    public ResponseEntity<MessageResponse> updateSemester(@PathVariable Long semesterId,
                                                          @Valid @RequestBody Semesters semesterDetails) {
        try {
            Semesters semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new RuntimeException("Semester not found"));

            semester.setName(semesterDetails.getName());
            semester.setStartDate(semesterDetails.getStartDate());
            semester.setEndDate(semesterDetails.getEndDate());
            semester.setActive(semesterDetails.getActive());

            semesterRepository.save(semester);
            return ResponseEntity.ok(new MessageResponse("Semester updated successfully!", "SUCCESS", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating semester: " + e.getMessage(),"ERROR",null));
        }
    }

    @DeleteMapping("/semesters/{semesterId}")
    @Operation(summary = "Delete semester", description = "Delete a semester")
    public ResponseEntity<MessageResponse> deleteSemester(@PathVariable Long semesterId) {
        try {
            Semesters semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new RuntimeException("Semester not found"));
            semesterRepository.delete(semester);
            return ResponseEntity.ok(new MessageResponse("Semester deleted successfully!", "SUCCESS",null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error deleting semester: " + e.getMessage(),"ERROR", null));
        }
    }
}
