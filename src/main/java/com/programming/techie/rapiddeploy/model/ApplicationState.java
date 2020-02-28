package com.programming.techie.rapiddeploy.model;

public enum ApplicationState {
    RUNNING("Running"),
    STOPPED("Stopped"),
    FAILED("");

    private String value;

    ApplicationState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
