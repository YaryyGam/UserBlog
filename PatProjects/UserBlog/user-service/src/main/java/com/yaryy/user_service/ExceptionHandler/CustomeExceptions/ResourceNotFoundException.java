package com.yaryy.user_service.ExceptionHandler.CustomeExceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

