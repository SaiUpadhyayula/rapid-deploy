package com.programming.techie.rapiddeploy.model;

public enum SupportedLanguage {
    JAVA("java"),
    NODEJS("node-js"),
    PYTHON("python"),
    PHP("php"),
    RUBY("ruby");

    private String value;

    SupportedLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
