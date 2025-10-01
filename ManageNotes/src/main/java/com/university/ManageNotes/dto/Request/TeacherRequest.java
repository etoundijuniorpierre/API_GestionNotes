package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.TeachingLevel;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TeacherRequest {
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
// This regular expression pattern validates phone numbers:
// ^ - start of string
// \+? - optional plus sign for international numbers
// [1-9] - first digit must be 1-9 (no leading zero)
// \d{1,14} - followed by 1-14 digits
// $ - end of string
// Examples of valid numbers:
// +1234567890 (international)
// 1234567890 (local)
// +44123456789 (UK number)
@Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    private String phoneNumber;

    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    private List<Subject> subjects;
    
    @NotNull(message = "Department is required")
    private Department department;
    
    @NotEmpty(message = "At least one teaching level is required")
    private List<TeachingLevel> teachingLevel;
    
    private Roles appRole;
    
    private Boolean isActive;
}