package com.university.ManageNotes.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public abstract class UtilisateurRequestDto {
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    private String prenom;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Le format de l'email est invalide")
    private String email;

    private String telephone;

    // Password should ideally be handled separately for security (e.g., dedicated change password DTO)
    // For simplicity here, we include it, but be mindful in a real app.
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;
}

