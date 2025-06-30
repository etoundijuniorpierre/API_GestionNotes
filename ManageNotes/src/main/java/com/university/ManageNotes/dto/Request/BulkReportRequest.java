package com.university.ManageNotes.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkReportRequest {

    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Semester ID is required")
    private Long semesterId;

    private String reportType = "TRANSCRIPT";
    private String format = "PDF";
    private Boolean includeComments = true;
    private List<String> recipientEmails; // For bulk email delivery

}
