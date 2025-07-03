package com.university.ManageNotes.dto.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, List<String>> validationErrors;

    public ValidationErrorResponse(Map<String, List<String>> validationErrors) {
        super("Validation Failed", "One or more fields have validation errors", 400);
        this.validationErrors = validationErrors;
        this.setTimestamp(LocalDateTime.now());
    }

    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, List<String>> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
