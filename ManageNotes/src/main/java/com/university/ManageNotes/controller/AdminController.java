package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.UserRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            if (userRequest.getPassword() == null || userRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(MessageResponse.error("Password is required"));
            }

            UserResponse createdUser = userService.createUser(userRequest);
            return ResponseEntity.ok(MessageResponse.success("User created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to create user: " + e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequest userRequest) {
        try {
            if (userRequest.getPassword() != null && !userRequest.getPassword().trim().isEmpty()) {
                // Only validate password if it's being updated
                if (userRequest.getPassword().length() < 6) {
                    return ResponseEntity.badRequest()
                            .body(MessageResponse.error("Password must be at least 6 characters"));
                }
            }

            UserResponse updatedUser = userService.updateUser(userId, userRequest);
            return ResponseEntity.ok(MessageResponse.success("User updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to update user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(MessageResponse.success("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to delete user: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to retrieve users: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserResponse user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to retrieve user: " + e.getMessage()));
        }
    }

    @PostMapping("/users/{userId}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        try {
            userService.activateUser(userId);
            return ResponseEntity.ok(MessageResponse.success("User activated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to activate user: " + e.getMessage()));
        }
    }

    @PostMapping("/users/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        try {
            userService.deactivateUser(userId);
            return ResponseEntity.ok(MessageResponse.success("User deactivated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to deactivate user: " + e.getMessage()));
        }
    }

    @PostMapping("/system/backup")
    public ResponseEntity<?> createBackup() {
        try {
            // Backup logic here
            return ResponseEntity.ok(MessageResponse.success("Backup created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to create backup: " + e.getMessage()));
        }
    }

    @PostMapping("/system/restore")
    public ResponseEntity<?> restoreBackup(@RequestParam String backupPath) {
        try {
            // Restore logic here
            return ResponseEntity.ok(MessageResponse.success("System restored successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to restore system: " + e.getMessage()));
        }
    }
}
