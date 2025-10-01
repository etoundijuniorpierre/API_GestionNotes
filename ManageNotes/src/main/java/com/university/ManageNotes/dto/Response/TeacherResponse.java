package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.TeachingLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long teacherId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private List<SubjectResponse> subjects;
    private Department department;
    private Set<TeachingLevel> teachingLevel;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Roles appRole;
    private Boolean isActive;
}
