package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Roles roles;

    private Boolean isActive;

    private Instant createdDate;
    private Instant lastModifiedDate;
}
