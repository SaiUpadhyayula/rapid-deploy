package com.programming.techie.rapiddeploy.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RapidDeployExceptionHandler {

    @ExceptionHandler(RapidDeployException.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
