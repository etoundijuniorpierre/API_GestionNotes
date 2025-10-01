package com.university.ManageNotes.model;

import com.university.ManageNotes.model.enums.StudentLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teaching_levels")
public class TeachingLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teachingLevelId;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    @Column(length = 20)
    private StudentLevel studentLevel;
}
