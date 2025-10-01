package com.university.ManageNotes.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    Long fieldId;
    String fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldValue, fieldName));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("%s not found with %s : '%d'", resourceName, fieldName, fieldId));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }
}
