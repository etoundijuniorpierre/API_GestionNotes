package com.university.ManageNotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "semester")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long semesterId;

    @Column(unique = true)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "isActive")
    private Boolean active;

    @OneToMany(mappedBy = "semester", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Subject> subjects = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "semester", fetch = FetchType.LAZY)
    private List<Grades> grades = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "semester", fetch = FetchType.LAZY)
    private List<Revendication> revendications = new ArrayList<>();

    @CreatedDate
    @Column(name ="creation_date",nullable = false,updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}
