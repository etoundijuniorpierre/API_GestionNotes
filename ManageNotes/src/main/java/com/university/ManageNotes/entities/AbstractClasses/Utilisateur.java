package com.university.ManageNotes.entities.AbstractClasses;

import com.university.ManageNotes.entities.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@NoArgsConstructor(force = true)
public abstract class Utilisateur extends BaseEntity {
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String telephone;

    @Column(nullable = false)
    @Size(min = 8, max = 16)
    private String motDePasse;

    @OneToMany(mappedBy = "utilisateur")
    private List<Role> roles = new ArrayList<>();
}
