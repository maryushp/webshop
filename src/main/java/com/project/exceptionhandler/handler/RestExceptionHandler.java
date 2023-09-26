package com.project.exceptionhandler.handler;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.core.Ordered;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElemException.class)
    public ResponseEntity<Object> handleNoSuchElementException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }

    @ExceptionHandler(SuchElementAlreadyExists.class)
    public ResponseEntity<Object> handleSuchElementAlreadyExists(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(InvalidElementException.class)
    public ResponseEntity<Object> handleInvalidElementException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
