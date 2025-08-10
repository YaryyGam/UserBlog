package com.yaryy.user_service.ExceptionHandler.CustomeExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){super(message);}
}
