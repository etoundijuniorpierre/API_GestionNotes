package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.JwtResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.JwtUtils;
import com.university.ManageNotes.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(),
                              userDetails.getFirstName(), userDetails.getLastName(), userDetails.getAuthorities(),userDetails.getUsername());
    }

    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!", "ERROR", null);
        }

        // Create new user account
        Users user = new Users();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setActive(true);

        // Set role - default to STUDENT if not specified
        Role role = signUpRequest.getRole() != null ? signUpRequest.getRole() : Role.STUDENT;
        user.setRole(role);

        userRepository.save(user);

        return new MessageResponse("User registered successfully!", "SUCCESS", null);
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userRepository.findByEmail(userPrincipal.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user found");
    }
}
