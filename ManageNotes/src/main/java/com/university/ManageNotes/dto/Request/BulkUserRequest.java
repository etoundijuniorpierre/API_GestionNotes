package com.university.ManageNotes.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkUserRequest {
    @NotEmpty(message = "User list cannot be empty")
    @Valid
    private List<SignupRequest> users;

    private Boolean sendWelcomeEmail = true;
    private Boolean generatePasswords = false;
}
