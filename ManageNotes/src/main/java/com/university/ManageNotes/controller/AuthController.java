package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.PasswordChangeRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.LoginResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.service.AuthService;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "Authenticate a user information", description = "This endpoint authenticate a particular user from the login credentials")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/admin/register")
    @Operation(summary = "Register a user information", description = "This endpoint is an Admin priviledge to Register a particular user")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody SignupRequest signupRequest) {
        return new ResponseEntity<>(authService.register(signupRequest), HttpStatus.CREATED);
    }

    @GetMapping("/admin/profile")
    @Operation(summary = "Get current admin profile", description = "This endpoint provide the informations of the current logged in admin")
    public ResponseEntity<UserResponse> getAdminDetails(Authentication authentication) {
        UserResponse userProfileReponse = authService.getCurrentAdmin(authentication);
        return new ResponseEntity<>(userProfileReponse, HttpStatus.OK);
    }

    @PostMapping("/password")
    @Operation(summary = "Update a user's password information", description = "This endpoint update a particular user's password")
    public ResponseEntity<MessageResponse> changePassword(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                                            @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        MessageResponse response = authService.changePassword(
                userPrincipal.getUsername(),
                passwordChangeRequest.getNewPassword()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out a user", description = "This endpoint log out a particular user from the system")
    public ResponseEntity<MessageResponse> logout() {
        MessageResponse response = authService.logout();
        return ResponseEntity.ok()
                .body(response);
    }
}
