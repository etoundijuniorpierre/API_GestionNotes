package com.university.ManageNotes.controller;

import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.dto.Response.UserProfileResponse;
import com.university.ManageNotes.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Lookup", description = "Utility endpoints for user/role lookup")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public com.university.ManageNotes.dto.Response.UserProfileResponse me(Authentication authentication) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElseThrow();
        return com.university.ManageNotes.dto.Response.UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "List all students")
    public java.util.List<UserProfileResponse> students() {
        return userRepository.findByRole(Role.STUDENT).stream()
                .map(u -> UserProfileResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .build())
                .toList();
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all teachers")
    public java.util.List<UserProfileResponse> teachers() {
        return userRepository.findByRole(Role.TEACHER).stream()
                .map(u -> UserProfileResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .build())
                .toList();
    }
}
