package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    private Role role;
    private List<String> authorities;


}
