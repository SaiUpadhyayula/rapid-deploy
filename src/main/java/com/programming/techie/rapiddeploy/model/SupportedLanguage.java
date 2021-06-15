package com.programming.techie.rapiddeploy.model;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;

import java.util.Arrays;

public enum SupportedLanguage {
    JAVA("java"),
    NODEJS("node-js"),
    PYTHON("python"),
    PHP("php"),
    RUBY("ruby"),
    HTML("html");

    private final String value;

    SupportedLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SupportedLanguage lookup(String value) {
        return Arrays.stream(SupportedLanguage.values())
                .filter(enumValue -> enumValue.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new RapidDeployException("Cannot find enumeration for value - " + value));
    }
}
