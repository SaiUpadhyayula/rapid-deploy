package com.programming.techie.rapiddeploy.exceptions;

public class RapidDeployException extends RuntimeException {
    public RapidDeployException(String message, Exception e) {
        super(message, e);
    }

    public RapidDeployException(String message) {
        super(message);
    }
}
