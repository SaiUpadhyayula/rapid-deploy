package com.programming.techie.rapiddeploy.model;

public enum BuildState {
    SUCCESS("Success"),
    IN_PROGRESS("In Progress"),
    FAILED("Failed");

    private String value;

    BuildState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
