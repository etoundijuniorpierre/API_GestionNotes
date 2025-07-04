package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.JwtResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.mapper.UserMapper;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

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

        Users saved = userRepository.save(user);

        return new MessageResponse(
                "User registered successfully!",
                "SUCCESS",
                userMapper.toUserResponse(saved)
        );
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Users userDetails = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            return new JwtResponse(jwt,
                    "Bearer",
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getRole());
        } else {
            throw new RuntimeException("Error: User not authenticated.");
        }
    }

    public MessageResponse changePassword(String username, String oldPassword, String newPassword) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return MessageResponse.error("Error: Incorrect old password!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return MessageResponse.success("Password changed successfully!");
    }

    public Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        } else {
            throw new RuntimeException("Error: User not authenticated.");
        }
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
