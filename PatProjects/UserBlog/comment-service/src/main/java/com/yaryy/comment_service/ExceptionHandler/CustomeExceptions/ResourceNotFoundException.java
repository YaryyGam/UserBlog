package com.yaryy.comment_service.ExceptionHandler.CustomeExceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

