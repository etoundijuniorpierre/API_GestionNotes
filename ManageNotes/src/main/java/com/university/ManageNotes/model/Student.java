package com.university.ManageNotes.model;

import com.university.ManageNotes.model.enums.StudentCycle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "place_of_birth"),
                @UniqueConstraint(columnNames = "matricule")
        })
public class Student extends Users {
    @OneToOne
    @JoinColumn(name = "level_id")
    private TeachingLevel studentLevel;

    @Column(name = "matricule")
    private String matricule;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Grades> grades = new ArrayList<>();

    @Column(name = "speciality")
    private String speciality;

    @Enumerated(EnumType.STRING)
    private StudentCycle cycle;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @OneToOne(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private Transcript transcript;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    orphanRemoval = true)
    private List<Revendication> revendications = new ArrayList<>();
}
