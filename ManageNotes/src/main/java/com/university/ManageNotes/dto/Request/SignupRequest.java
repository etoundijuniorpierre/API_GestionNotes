package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.StudentCycle;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    private Set<Roles> role;

    // Student fields
    private TeachingLevel level;  // Single level for students
    private String matricule;
    private String speciality;
    private StudentCycle cycle;
    private LocalDate dateOfBirth;
    private String placeOfBirth;

    // Teacher fields
    private List<TeachingLevel> levels;
    private Department department;
    private String phone;
    private List<Subject> subjects;
}
