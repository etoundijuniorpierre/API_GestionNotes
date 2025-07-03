package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {
    private Long id;
    private String action; // "CREATE", "UPDATE", "DELETE", "LOGIN", "LOGOUT"
    private String entityType; // "USER", "GRADE", "SUBJECT", etc.
    private Long entityId;
    private String entityName;
    private Long userId;
    private String username;
    private String ipAddress;
    private String userAgent;
    private String oldValues;
    private String newValues;
    private LocalDateTime timestamp;
    private String description;
}

