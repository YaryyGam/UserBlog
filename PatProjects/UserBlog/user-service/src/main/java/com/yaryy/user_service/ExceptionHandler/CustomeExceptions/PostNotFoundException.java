package com.yaryy.user_service.ExceptionHandler.CustomeExceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message){super(message);}
}
