package com.project.utils.exceptionhandler.exceptions;

public class InvalidUpdateRequest extends ApplicationException {
    public InvalidUpdateRequest(String message) {
        super(message);
    }
}