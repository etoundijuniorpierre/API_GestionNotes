package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.UserRequest;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public UserResponse createUser(UserRequest userRequest) {
        try {
            // Create user logic here
            // Hash password
            // Save to database
            // Convert to response

            UserResponse response = new UserResponse();
            response.setUsername(userRequest.getUsername());
            response.setEmail(userRequest.getEmail());
            response.setFirstName(userRequest.getFirstName());
            response.setLastName(userRequest.getLastName());
            response.setPhone(userRequest.getPhone());
            response.setRole(userRequest.getRole());
            response.setActive(userRequest.getActive());

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        try {
            // Update user logic here
            // Find existing user
            // Update fields
            // Save to database
            // Convert to response

            UserResponse response = new UserResponse();
            response.setId(userId);
            response.setUsername(userRequest.getUsername());
            response.setEmail(userRequest.getEmail());
            response.setFirstName(userRequest.getFirstName());
            response.setLastName(userRequest.getLastName());
            response.setPhone(userRequest.getPhone());
            response.setRole(userRequest.getRole());
            response.setActive(userRequest.getActive());

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public void deleteUser(Long userId) {
        try {
            // Delete user logic here
            // Find user
            // Delete from database
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }

    public List<UserResponse> getAllUsers() {
        try {
            // Get all users logic here
            // Retrieve from database
            // Convert to response list
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve users: " + e.getMessage());
        }
    }

    public UserResponse getUserById(Long userId) {
        try {
            // Get user by ID logic here
            // Find in database
            // Convert to response

            UserResponse response = new UserResponse();
            response.setId(userId);
            // Set other fields from database

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve user: " + e.getMessage());
        }
    }

    public void activateUser(Long userId) {
        try {
            // Activate user logic here
            // Find user
            // Set active = true
            // Save to database
        } catch (Exception e) {
            throw new RuntimeException("Failed to activate user: " + e.getMessage());
        }
    }

    public void deactivateUser(Long userId) {
        try {
            // Deactivate user logic here
            // Find user
            // Set active = false
            // Save to database
        } catch (Exception e) {
            throw new RuntimeException("Failed to deactivate user: " + e.getMessage());
        }
    }

    private UserResponse convertToResponse(Users user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setActive(user.getActive());
        return response;
    }
}
