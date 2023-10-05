package com.project.exceptionhandler.handler;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.core.Ordered;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(InvalidElementException.class)
    public ResponseEntity<Problem> handleInvalidElementException(InvalidElementException ex) {
        Problem problem = Problem.builder().withTitle("Bad Request").withStatus(Status.BAD_REQUEST).withDetail(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
}