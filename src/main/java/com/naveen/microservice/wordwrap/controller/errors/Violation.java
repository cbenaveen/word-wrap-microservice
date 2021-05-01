package com.naveen.microservice.wordwrap.controller.errors;

public final class Violation {
    private final String fieldName;
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getMessage() {
        return this.message;
    }
}
