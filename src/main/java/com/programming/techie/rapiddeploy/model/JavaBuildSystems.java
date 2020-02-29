package com.programming.techie.rapiddeploy.model;

public enum JavaBuildSystems {
    MAVEN("Maven"),
    GRADLE("Gradle");

    private String value;

    JavaBuildSystems(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
