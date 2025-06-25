package com.university.ManageNotes.entities;



import com.university.ManageNotes.entities.AbstractClasses.Utilisateur;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor(force = true)
public class Administrateur extends Utilisateur {

}
