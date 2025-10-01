package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.TeachingLevel;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    private Roles appRole;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @NotNull(message = "Student level is required")
    private TeachingLevel studentLevel;
    
    @NotNull(message = "Cycle is required")
    private StudentCycle cycle;
    
    @NotBlank(message = "Matricule is required")
    @Pattern(regexp = "^[0-9]{2}[A-Z][0-9]{4}$", message = "Matricule must be 7 characters: 2 digits + 1 uppercase letter + 4 digits")
    private String matricule;
    
    @NotBlank(message = "Speciality is required")
    @Size(min = 3, max = 100, message = "Speciality must be between 3 and 100 characters")
    private String speciality;
    
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Place of birth is required")
    @Size(min = 2, max = 100, message = "Place of birth must be between 2 and 100 characters")
    private String placeOfBirth;
}