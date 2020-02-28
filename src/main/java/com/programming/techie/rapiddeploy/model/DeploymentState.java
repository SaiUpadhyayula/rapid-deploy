package com.programming.techie.rapiddeploy.model;

public enum DeploymentState {
    SUCCESS("Success"),
    FAILED("Failed");

    private String value;

    DeploymentState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
