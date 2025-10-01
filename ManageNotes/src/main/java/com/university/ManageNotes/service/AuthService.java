package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.LoginResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
    
    MessageResponse register(SignupRequest signupRequest);
    
    UserResponse getCurrentAdmin(Authentication authentication);
    
    MessageResponse changePassword(String username, String newPassword);
    
    MessageResponse logout();
}
