package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.JwtResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.mapper.UserMapper;
import com.university.ManageNotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public MessageResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return MessageResponse.error("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return MessageResponse.error("Error: Email is already in use!");
        }

        // Create new user's account
        Users user = userMapper.toUser(signupRequest);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setActive(true);

        userRepository.save(user);

        return MessageResponse.success("User registered successfully!");
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        try {
            // Authentication logic here
            // Validate credentials
            // Generate JWT token

            return new JwtResponse("sample-jwt-token", "Bearer",
                    loginRequest.getUsername(), "sample-email@example.com", Role.STUDENT);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public MessageResponse changePassword(String username, String oldPassword, String newPassword) {
        try {
            // Password change logic here
            // Validate old password
            // Update with new password

            return MessageResponse.success("Password changed successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Password change failed: " + e.getMessage());
        }
    }

    public Users getCurrentUser() {
        // This would typically get the current user from SecurityContext
        // For now, returning a placeholder
        Users currentUser = new Users();
        currentUser.setUsername("current_user");
        currentUser.setEmail("current@example.com");
        currentUser.setFirstName("Current");
        currentUser.setLastName("User");
        return currentUser;
    }

    public String getCurrentUsername() {
        // Get current username from security context
        return "current_user"; // Placeholder
    }
}
