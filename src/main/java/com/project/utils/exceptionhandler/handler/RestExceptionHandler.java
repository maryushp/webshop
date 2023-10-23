package com.project.utils.exceptionhandler.handler;

import com.project.utils.exceptionhandler.exceptions.InvalidUpdateRequest;
import com.project.utils.exceptionhandler.exceptions.NoSuchElemException;
import com.project.utils.exceptionhandler.exceptions.SuchElementAlreadyExists;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.core.Ordered;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import static com.project.utils.exceptionhandler.ExceptionMessages.INVALID_ENTITY;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NoSuchElemException.class)
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElemException ex) {
        Problem problem = Problem.builder().withTitle("Not Found").withStatus(Status.NOT_FOUND).withDetail(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(SuchElementAlreadyExists.class)
    public ResponseEntity<Problem> handleSuchElementAlreadyExists(SuchElementAlreadyExists ex) {
        Problem problem = Problem.builder().withTitle("Conflict").withStatus(Status.CONFLICT).withDetail(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : INVALID_ENTITY;
        Problem problem = Problem.builder().withTitle("Bad Request").withStatus(Status.BAD_REQUEST).withDetail(errorMessage).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Problem> badCredentialsException(BadCredentialsException ex) {
        Problem problem =
                Problem.builder().withTitle("Unauthorized").withStatus(Status.UNAUTHORIZED).withDetail(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(InvalidUpdateRequest.class)
    public ResponseEntity<Problem> handleInvalidUpdateRequest(InvalidUpdateRequest ex) {
        Problem problem =
                Problem.builder().withTitle("Bad Request").withStatus(Status.BAD_REQUEST).withDetail(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
}