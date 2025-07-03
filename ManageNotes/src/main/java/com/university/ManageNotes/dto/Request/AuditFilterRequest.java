package com.university.ManageNotes.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuditFilterRequest {
    private List<String> actions;
    private List<String> entityTypes;
    private List<Long> userIds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ipAddress;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "timestamp";
    private String sortDirection = "DESC";
}
