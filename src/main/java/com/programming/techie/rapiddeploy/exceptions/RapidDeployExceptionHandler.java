package com.programming.techie.rapiddeploy.exceptions;

import com.programming.techie.rapiddeploy.payload.BindingErrors;
import com.programming.techie.rapiddeploy.payload.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class RapidDeployExceptionHandler {

    @ExceptionHandler(RapidDeployException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException exception) {

        List<BindingErrors> bindingErrors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            bindingErrors.add(new BindingErrors(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return new ErrorResponse(bindingErrors);
    }
}
