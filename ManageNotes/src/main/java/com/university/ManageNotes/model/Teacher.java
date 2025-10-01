package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(name = "teachers")
public class Teacher extends Users {
    @Column(length = 9)
    private String phoneNumber;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subject> subjects;

    @OneToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "teacher_teaching_levels",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "teaching_level_id")
    )
    private List<TeachingLevel> teachingLevel = new ArrayList<>();


    @OneToMany(mappedBy = "examiner")
    private List<Grades> gradesEntered = new ArrayList<>();
}
