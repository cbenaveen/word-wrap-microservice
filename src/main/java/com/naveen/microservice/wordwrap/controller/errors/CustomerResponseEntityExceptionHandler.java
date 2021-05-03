package com.naveen.microservice.wordwrap.controller.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> badRequest(HttpServletRequest req, Exception exception) {
        return ResponseEntity.notFound().build();
    }
}
